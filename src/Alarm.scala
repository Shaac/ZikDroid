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

import android.app.{AlarmManager, PendingIntent}
import android.content.{BroadcastReceiver, Context, Intent}

import org.scaloid.common._

object Alarm {
  def set(implicit ctx: Context) {
    alarmManager.setInexactRepeating(
      AlarmManager.ELAPSED_REALTIME,
      AlarmManager.INTERVAL_HALF_HOUR,
      AlarmManager.INTERVAL_HALF_HOUR,
      PendingIntent.getBroadcast(ctx, 0, SIntent[AlarmReceiver], 0))
  }

}

class AlarmReceiver extends BroadcastReceiver {
  override def onReceive(context: Context, intent: Intent) {
    implicit val ctx = context
    context startService SIntent[BoundService]
  }
}
