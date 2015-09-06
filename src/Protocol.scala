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

object Protocol {
  // Byte array to send to open a session with device
  val start = Array[Byte](0, 0, 0)

  // Get byte array to send to get information corresponding to API request
  def getRequest(request: String): Array[Byte] = pack("GET " + request)

  // Get byte array to send to set values to corresponding API request
  def setRequest(request: String, arguments: String): Array[Byte] =
    pack("SET " + request + "?arg=" + arguments)

  // TODO function getting an XML from a byte array: array[7:]

  // Protocol is the following: first 2 bytes are packet size, then a minimal
  // value byte, and then the bytes of the string message
  private def pack(request: String): Array[Byte] = {
    val n = request.size + 3 // Entire size of the final byte array
    Array[Byte]((n >> 8).toByte, n.toByte, Byte.MinValue) ++ request.getBytes
  }
}

// They are a few others protocol features, for sending firmware on device for
// instance, but I would not risk trying it.
