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
import android.content.{BroadcastReceiver, Context, Intent, IntentFilter}

import org.scaloid.common._

class ZikDroid extends SActivity {
  val bluetooth = new LocalServiceConnection[BoundService]
  var zik: Option[BluetoothDevice] = None
  val filterBattery = new IntentFilter(Intents.BatteryUpdate)

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
      SButton("Battery") onClick { bluetooth(_.getBattery) }
      SButton("Enable noise cancellation") onClick { bluetooth(_.enableANC(true)) }
      SButton("Disable noise cancellation") onClick { bluetooth(_.enableANC(false)) }
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
  private val receiver = new BroadcastReceiver {
    def onReceive(context: Context, intent: Intent) {
      intent.getAction match {
        case Intents.BatteryUpdate =>
          bluetooth(_.getState map { x =>
          x.batteryLevel map { y => foo.text = "Battery: " + y}  })
      }
    }
  }

  override def onPause() {
    unregisterReceiver(receiver)
    super.onPause
  }
  override def onResume() {
    super.onResume
    registerReceiver(receiver, filterBattery)
  }
}
class AlarmReceiver extends BroadcastReceiver {
  def onReceive(context: Context, intent: Intent) {
    implicit val ctx = context
    context startService SIntent[BoundService]
  }
}

