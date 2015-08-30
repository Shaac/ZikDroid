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

import android.widget.Toast

class ZikDroid extends SActivity {
  var connection: Option[Connection] = None
  var zik: Option[BluetoothDevice] = None

  def getBattery {
    connection flatMap { _.getBattery } match {
      case Some(level) => foo.text = "Battery: " + level
      case None => reconnect
    }
  }

  def toast(message: String) =
    Toast.makeText(getApplicationContext, message, Toast.LENGTH_LONG).show

  def selectZik {
    Bluetooth.getZikDevices match {
      case Left(e) => toast("Bluetooth error: " + e)
      case Right(devices) =>
        zik = devices.size match {
          case 0 => None
          case _ => Some(devices.head) // TODO let user choose when > 1
        }
    }
  }

  def reconnect {
    connection map { _.disconnect }
    if (connection map { _.connect } getOrElse false)
      toast("Reconnected")
    else
      toast("Error trying to reconnect")
  }

  def connect {
    connection = zik map { device => new Connection(device) }
    if (zik.isEmpty)
      toast("No Zik to connect to")
    else if (connection map { _.connect} getOrElse false)
      toast("Connected to " + zik.get.getName)
    else
      toast("Error trying to connect to " + zik.get.getName)
  }

  lazy val foo = new STextView("This is ZikDroid")
  onCreate {
    contentView = new SVerticalLayout {
      style {
        case t: STextView => t textSize 20.dip
      }
      foo.here
      SButton("Reconnect") onClick reconnect
      SButton("Battery") onClick getBattery
    } padding 20.dip

    selectZik
    connect
  }
}
