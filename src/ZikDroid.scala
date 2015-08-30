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
    write(getRequest("/api/system/battery/get"))
    val battery = read map { xml => (xml \\ "battery" \ "@level").toString }
    battery map (value => foo.text = "Battery: " + value)
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

  def read: Option[scala.xml.Elem] = {
    skip(7)
    val data = new Array[Byte](1024)
    val size = input flatMap { x => Try(x read data).toOption }
    size map { new String(data, 0, _) } map { XML.loadString }
  }
  def skip(i: Int): Unit = Try(input map { _ skip i })
  def write(data: Array[Byte]): Unit = Try(output map { _ write data })

  def connect {
    zik map Bluetooth.connect match {
      case None => foo.text += "No Zik paired"
      case Some(Failure(e)) => toast("Connection error: " + e.getMessage)
      case Some(Success(socket)) =>
        output = Try(socket.getOutputStream).toOption
        input = Try(socket.getInputStream).toOption
        write(Array[Byte](0, 3, 0))
        skip(1024)
    }
  }

  lazy val foo = new STextView("This is ZikDroid")
  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
      SButton("Connect") onClick connect
      SButton("Battery") onClick getBattery
    } padding 20.dip

    selectZik
    connect
  }
}
