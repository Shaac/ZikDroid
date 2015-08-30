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
  var connection: Option[Connection] = None
  var zik: Option[BluetoothDevice] = None

  def getBattery {
    connection flatMap { _.getBattery } match {
      case Some(level) => foo.text = "Battery: " + level
      case None => reconnect
    }
  }

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

  def reconnect {
    connection map { _.disconnect }
    if (connection map { _.connect } getOrElse false)
      longToast("Reconnected")
    else
      longToast("Error trying to reconnect")
  }

  def connect {
    connection = zik map { device => new Connection(device) }
    if (zik.isEmpty)
      longToast("No Zik to connect to")
    else if (connection map { _.connect} getOrElse false)
      longToast("Connected to " + zik.get.getName)
    else
      longToast("Error trying to connect to " + zik.get.getName)
  }

  lazy val foo = new STextView("This is ZikDroid")
  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
      SButton("Reconnect") onClick reconnect
      SButton("Battery") onClick getBattery
    } padding 20.dip

    selectZik
    connect
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
    Log.i("ZikDroid", "ALARM!")
    Toast.makeText(context, "alarm hit", Toast.LENGTH_SHORT).show
  }
}
