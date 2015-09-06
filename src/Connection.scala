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

import android.annotation.TargetApi
import android.content.Context
import android.bluetooth.{BluetoothAdapter, BluetoothDevice, BluetoothSocket}

import java.io.{InputStream, OutputStream}

import scala.util.{Try, Success, Failure}

import Protocol._

class Connection(device: BluetoothDevice, context: Context) {
  private var adapter: Option[BluetoothAdapter] = Bluetooth.getAdapter
  private var socket: Option[BluetoothSocket] = None
  private var input: Option[InputStream] = None
  private var output: Option[OutputStream] = None
  val state: State = new State
  private val parser: Parser = new Parser(state, context)

  def connect: Boolean =
    Bluetooth connect device match {
      case Failure(e) => false
      case Success(sock) =>
        socket = Some(sock)
        output = Try(sock.getOutputStream).toOption
        input = Try(sock.getInputStream).toOption
        write(Array[Byte](0, 3, 0)) && skip(1024)
    }

  def reconnect: Boolean = {
    disconnect
    connect
  }

  def disconnect {
    Try(socket map { _.close })
    socket = None
    input = None
    output = None
  }

  def getBattery: Option[Int] = {
    write(getRequest(API.BatteryGet))
    read
    state.batteryLevel
  }

  def enableANC(enable: Boolean): Option[Unit] = {
    write(setRequest(API.ANCEnableSet, enable.toString))
    read
    if (input.isEmpty) None else Some(Unit)
  }

  private def read {
    skip(7)
    val data = new Array[Byte](1024)
    val size = input flatMap { x => Try(x read data).toOption }
    size map { new String(data, 0, _) } map parser.parse
  }
  private def skip(i: Int): Boolean =
    Try(input map { _ skip i }).toOption != None
  private def write(data: Array[Byte]): Boolean =
    Try(output map { _ write data }).toOption != None
}
