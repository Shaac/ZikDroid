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
  private val bluetooth = new LocalServiceConnection[BoundService]
  private lazy val battery = new STextView

  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      battery.here
      SButton("Battery") onClick { bluetooth(_.getBattery) }
      SButton("Enable noise cancellation") onClick { bluetooth(_.enableANC(true)) }
      SButton("Disable noise cancellation") onClick { bluetooth(_.enableANC(false)) }
    } padding 20.dip

    bluetooth(_.selectZik)
    bluetooth(_.connect)
    refresh(bluetooth(_.getState, None))
    Alarm.set
  }

  broadcastReceiver(new IntentFilter(Intents.BatteryUpdate)) {
    (context, intent) => refresh(bluetooth(_.getState, None))
  }

  def refresh(state: Option[State]) {
    state flatMap { _.batteryLevel } match {
      case None => battery text "Battery: unknown"
      case Some(level) => battery text ("Battery: " + level + "%")
    }
  }
}
