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

import android.bluetooth.BluetoothDevice

import org.scaloid.common._

import Bluetooth._
import Protocol._

import android.util.Log
import android.widget.Toast

import java.io.{InputStream, OutputStream}

import scala.util.{Try, Success, Failure}
import scala.xml._

class ZikDroid extends SActivity {
  var output: Option[OutputStream] = None
  var input: Option[InputStream] = None
  var zik: Option[BluetoothDevice] = None

  def getBattery {
    Log.i("ZikDroid", "Getting battery status")
    output map { _ write getRequest("/api/system/battery/get") }
  }

  def readSocket {
    input map { _ skip 7 }
    val data = new Array[Byte](1024)
    val value = input map { _ read data } map { new String(data, 0, _) }
    val xml = value map XML.loadString
    if (xml.isDefined) {
      val battery = (xml.get \\ "battery" \ "@level").toString
      foo.text = "Battery: " + battery
    }
  }

  def toast(message: String) =
    Toast.makeText(getApplicationContext, message, Toast.LENGTH_LONG).show

  def selectZik {
    getZikDevices match {
      case Left(e) => toast("Bluetooth error: " + e)
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
      SButton("Battery") onClick getBattery
      SButton("Read") onClick readSocket
    } padding 20.dip

    selectZik
    zik map Bluetooth.connect match {
      case None => foo.text += "No Zik paired"
      case Some(Failure(e)) => toast("Connection error: " + e.getMessage)
      case Some(Success(socket)) =>
        output = Try(socket.getOutputStream).toOption
        input = Try(socket.getInputStream).toOption
        output map { _ write Array[Byte](0, 3, 0) }
        input map { _ skip 1024 }
    }
  }
}
