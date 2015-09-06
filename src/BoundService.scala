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

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.Builder

import org.scaloid.common._

class BoundService extends LocalService {
  private var connection: Option[Connection] = None

  def getState: Option[State] = connection map { _.state }

  def associate(device: BluetoothDevice) {
    connection map { _.disconnect }
    connection = Some(new Connection(device, this))
  }
  def connect: Boolean = connection map { _.connect } getOrElse false
  def reconnect: Boolean = {
    connection map { _.disconnect }
    connect
  }
  def getBattery: Option[Int] =
    connection flatMap { _.getBattery } match {
      case Some(level) => Some(level)
      case None =>
        selectZik
        if (reconnect) getBattery else None
    }
  def enableANC(enable: Boolean) {
    connection flatMap { _.enableANC(enable) } match {
      case Some(_) => {}
      case None =>
        selectZik
        if (reconnect) enableANC(enable)
    }
  }
  def selectZik {
    Bluetooth.getZikDevices match {
      case Left(e) => longToast("Bluetooth error: " + e)
      case Right(devices) =>
        devices.size match {
          case 0 => {}
          case _ => associate(devices.head) // TODO let user choose when > 1
        }
    }
  }
  override def onStartCommand(intent: Intent, x: Int, y: Int) = {
    getBattery match {
      case None => longToast("Error getting battery")
      case Some(level) => if (level <= 20) {
        val builder = new Builder(this)
          .setSmallIcon(R.drawable.ic_notify)
          .setLargeIcon(
            BitmapFactory.decodeResource(getResources, R.drawable.ic_launcher))
          .setContentTitle("ZikDroid")
          .setContentText("Battery level: " + level + "%")
          .setAutoCancel(true)
        builder setContentIntent pendingActivity[ZikDroid];
        notificationManager.notify(1, builder.build);
      } else notificationManager.cancelAll
    }
  1
  }
}
