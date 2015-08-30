/* This file is part of ZikDroid.
 * Copyright (C) 2015 Sacha Delanoue <contact@shaac.me>
 *
 * ZikDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ZikDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ZikDroid.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.shaac.zikdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothDevice
import android.content.{BroadcastReceiver, Context, Intent}
import android.util.Log
import android.os.SystemClock

import org.scaloid.common._

import android.widget.Toast

class ZikDroid extends SActivity {
  val bluetooth = new LocalServiceConnection[MyService]
  var zik: Option[BluetoothDevice] = None

  def selectZik {
    Bluetooth.getZikDevices match {
      case Left(e) => longToast("Bluetooth error: " + e)
      case Right(devices) =>
        zik = devices.size match {
          case 0 => None
          case _ => Some(devices.head) // TODO let user choose when > 1
        }
    }
  }

  lazy val foo = new STextView("This is ZikDroid")
  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
      SButton("Reconnect") onClick { bluetooth(_.reconnect) }
      SButton("Battery") onClick { bluetooth(_.getBattery) }
    } padding 20.dip

    selectZik
    zik map { device => bluetooth(_.associate(device)) }
    bluetooth(_.connect)
    val intent = PendingIntent.getBroadcast(this, 0, SIntent[AlarmReceiver], 0)
    val am = getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]
    am.setInexactRepeating(
      AlarmManager.ELAPSED_REALTIME_WAKEUP,
      AlarmManager.INTERVAL_FIFTEEN_MINUTES,
      AlarmManager.INTERVAL_FIFTEEN_MINUTES,
      intent)
  }
}
class AlarmReceiver extends BroadcastReceiver {
  def onReceive(context: Context, intent: Intent) {
    implicit val ctx = context
    context startService SIntent[MyService]
  }
}

class MyService() extends LocalService {
  private var connection: Option[Connection] = None
  def associate(device: BluetoothDevice) {
    connection map { _.disconnect }
    connection = Some(new Connection(device))
  }
  def connect: Boolean = connection map { _.connect } getOrElse false
  def reconnect: Boolean = {
    connection map { _.disconnect }
    connect
  }
  def getBattery {
    connection flatMap { _.getBattery } match {
      case Some(level) => longToast("Battery: " + level)
      case None =>
        selectZik
        if (reconnect) getBattery else longToast("Reconnection failed")
    }
  }
  def selectZik {
    Bluetooth.getZikDevices match {
      case Left(e) => longToast("Bluetooth error: " + e)
      case Right(devices) =>
        devices.size match {
          case 0 => {}
          case _ => associate(devices.head)
        }
    }
  }
  override def onStartCommand(intent: Intent, x: Int, y: Int) = {
    getBattery
    1
  }
}
