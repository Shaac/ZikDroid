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

import scala.util.Try

object Protocol {
  val BATTERY = "/api/system/battery/get"

  def batteryLevel(xml: scala.xml.Elem): Try[Int] =
    Try((xml \\ "battery" \ "@level").toString.toInt)

  private def pack(request: String): Array[Byte] =
    Array[Byte](0, (request.size + 3).toByte, 0x80.toByte) ++ request.getBytes

  def getRequest(command: String): Array[Byte] = pack("GET " + command)

  def setRequest(command: String, args: String): Array[Byte] =
    pack("SET " + command + "?arg=" + args)
}
