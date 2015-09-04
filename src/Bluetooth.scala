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

import android.bluetooth.{BluetoothAdapter, BluetoothDevice, BluetoothSocket}

import java.util.UUID.fromString

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.util.Try

object Bluetooth {
  // Parrot Zik headphones MAC address regex (90:03:B7 is Parrot vendor id)
  private val MAC = "90:03:B7:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}"

  // UUID to communicate with Parrot Zik devices
  private val UUID = fromString("0ef0f502-f0ee-46c9-986c-54ed027807fb")

  def getAdapter: Option[BluetoothAdapter] =
    BluetoothAdapter.getDefaultAdapter match {
      case adapter: BluetoothAdapter => Some(adapter)
      case _ => None
    }

  private def getBondedDevices: Either[String, mutable.Set[BluetoothDevice]] =
    getAdapter match {
      case None => Left("No Bluetooth hardware.")
      case Some(adapter) => adapter.getBondedDevices match {
        case null => Left("Error getting bonded devices.")
        case set: java.util.Set[BluetoothDevice] => Right(set)
      }
    }

  def getZikDevices: Either[String, mutable.Set[BluetoothDevice]] =
    for (devices <- getBondedDevices.right)
      yield devices.filter(_.getAddress matches MAC)

  def createSocket(device: BluetoothDevice): Try[BluetoothSocket] =
    Try(device createRfcommSocketToServiceRecord UUID)

  def connect(device: BluetoothDevice): Try[BluetoothSocket] = {
    // TODO use BluetoothAdapter.isDiscovering to cancel if discovering
    try {
      val socket = createSocket(device)
      socket.get.connect
      socket
    } catch {
      case e: java.io.IOException => Try(throw e)
    }
  }
}
