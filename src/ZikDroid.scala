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

import Bluetooth._
import Protocol._

import android.util.Log
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.OutputStream

import scala.xml._

class ZikDroid extends SActivity {
  var output: OutputStream = null
  var input: InputStream = null

  def getBattery {
    Log.i("ZikDroid", "Getting battery status")
    output.write(getRequest("/api/system/battery/get"))
  }

  def readSocket {
    val data = new Array[Byte](1024)
    input.read(data, 0, 7)
    val n = input.read(data)
    val value : String = new String(data, 0, n)
    val xml = XML.loadString(value)
    val battery = (xml \\ "battery" \ "@level").toString
    foo.text = "Battery: " + battery
  }

    lazy val foo = new STextView("This is ZikDroid")
  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
      SButton("Battery") onClick getBattery
      SButton("Read") onClick readSocket
    } padding 20.dip
    val devices = getZikDevices(getApplicationContext)
    devices foreach {i => foo.text += "\n" + i.getName}
    val zik = devices.head
    val socket = zik.createRfcommSocketToServiceRecord(Bluetooth.uuid)
    socket.connect
    output = socket.getOutputStream()
    output.write(Array[Byte](0, 3, 0))
    input = new BufferedInputStream(socket.getInputStream())
    val data = new Array[Byte](1024)
    val read = input.read(data)
  }
}
