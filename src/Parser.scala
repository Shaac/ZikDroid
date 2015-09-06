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

import android.content.Context
import android.util.Xml

import java.io.InputStream

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser._

import scala.util.Try

class Parser(state: State, implicit val ctx: Context) {
  def parse(xml: InputStream) {
    val parser = Xml.newPullParser
    parser.setInput(xml, null)
    handleEvent(parser)
  }

  def parse(s: String) { parse(new java.io.ByteArrayInputStream(s.getBytes)) }

  private def handleEvent(parser: XmlPullParser) {
    val event = parser.getEventType
    event match {
      case START_DOCUMENT => {}
      case END_DOCUMENT => 
      case START_TAG => parseTag(parser)
      case END_TAG => {}
      case TEXT => {}
    }
    if (event != END_DOCUMENT) {
      parser.next
      handleEvent(parser)
    }
  }

  private def parseTag(parser: XmlPullParser) {
    parser.getName.toLowerCase match {
      case "answer" => parseAnswer(parser)
    }
  }

  private def parseAnswer(parser: XmlPullParser) {
    parser.nextTag // Enter <answer>
    parser.getName.toLowerCase match {
      case "system" => parseSystem(parser)
    }
    parser.nextTag // Leave <answer>
  }

  private def parseSystem(parser: XmlPullParser) {
    parser.nextTag // Enter <system>
    parser.getName.toLowerCase match {
      case "battery" => parseBattery(parser)
    }
    parser.nextTag // Leave <system>
  }

  private def parseBattery(parser: XmlPullParser) {
    state.batteryLevel =
      Try(parser.getAttributeValue(null, "level").toInt).toOption
    state.batteryState = Try(parser.getAttributeValue(null, "state")).toOption
    Intents.broadcastBatteryUpdate
  }
}
