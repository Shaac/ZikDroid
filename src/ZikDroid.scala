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
 * along with this ZikDroid.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.shaac.zikdroid

import org.scaloid.common._
import android.os.Build
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.content.Context.BLUETOOTH_SERVICE

import scala.util.matching.Regex

import scala.collection.JavaConversions._

class ZikDroid extends SActivity {
  val mac = "90:03:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}"

  onCreate {
    lazy val foo = new STextView("This is ZikDroid")
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
    } padding 20.dip
    var adapter : BluetoothAdapter =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
        getSystemService(BLUETOOTH_SERVICE).asInstanceOf[BluetoothManager]
          .getAdapter
      else
        BluetoothAdapter.getDefaultAdapter
    val devices = adapter.getBondedDevices
    devices.filter(_.getAddress matches mac)
    devices foreach {i => foo.text += "\n" + i.getName}
  }
}
