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

import android.bluetooth.{BluetoothAdapter, BluetoothDevice, BluetoothSocket}

import java.util.UUID

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.util.Try

object Bluetooth {
  // Parrot Zik headphones MAC address regex
  private val mac =
    "90:03:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}"

  private val uuid = UUID.fromString("0ef0f502-f0ee-46c9-986c-54ed027807fb")

  private def getBondedDevices: Either[String, mutable.Set[BluetoothDevice]] =
    BluetoothAdapter.getDefaultAdapter match {
      case null => Left("No Bluetooth hardware.")
      case adapter: BluetoothAdapter => adapter.getBondedDevices match {
        case null => Left("Error getting bonded devices.")
        case set: java.util.Set[BluetoothDevice] => Right(set)
      }
    }

  def getZikDevices: Either[String, mutable.Set[BluetoothDevice]] =
    for (devices <- getBondedDevices.right)
      yield devices.filter(_.getAddress matches mac)

  def createSocket(device: BluetoothDevice): Try[BluetoothSocket] =
    Try(device createRfcommSocketToServiceRecord uuid)

  def connect(device: BluetoothDevice): Try[BluetoothSocket] =
    // TODO use BluetoothAdapter.isDiscovering to cancel if discovering
    createSocket(device).filter(socket => Try(socket.connect).isSuccess)
}
