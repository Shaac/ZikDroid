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

import java.util.UUID

import scala.collection.JavaConversions._
import scala.collection.mutable

object Bluetooth {
  // Parrot Zik headphones MAC address regex
  private val mac =
    "90:03:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}"

  val uuid = UUID.fromString("0ef0f502-f0ee-46c9-986c-54ed027807fb")

  // Get bluetooth adapter, or None if no bluetooth hardware
  private def getAdapter: Option[BluetoothAdapter] =
    BluetoothAdapter.getDefaultAdapter match {
      case adapter: BluetoothAdapter => Some(adapter)
      case _ => None
    }

  def getZikDevices: mutable.Set[BluetoothDevice] =
    getAdapter.get.getBondedDevices.filter(_.getAddress matches mac)
}
