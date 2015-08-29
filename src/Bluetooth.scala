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

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build

import scala.collection.JavaConversions._
import scala.collection.mutable

object Bluetooth {
  // Parrot Zik headphones MAC address regex
  private val mac =
    "90:03:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}"

  private def getAdapter(context: Context): BluetoothAdapter =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
      context.getSystemService(Context.BLUETOOTH_SERVICE)
        .asInstanceOf[BluetoothManager].getAdapter
    else
      BluetoothAdapter.getDefaultAdapter

  def getZikDevices(context: Context): mutable.Set[BluetoothDevice] =
    getAdapter(context).getBondedDevices.filter(_.getAddress matches mac)
}
