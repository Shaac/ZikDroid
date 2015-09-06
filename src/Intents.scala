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

import android.content.{Context, Intent}

object Intents {
  val Update = "me.shaac.zikdroid.UPDATE"

  def broadcastUpdate(implicit ctx: Context) {
    broadcastIntent(ctx, Update)
  }

  private def broadcastIntent(context: Context, name: String) {
    val intent = new Intent
    intent setAction name
    context sendBroadcast intent
  }
}
