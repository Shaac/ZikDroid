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

// This is API for Zik 1. There are more functions in Zik 2.
object API {

  /*********
   * Audio *
   *********/

  // Concert hall
  val SoundEffectGet         = "/api/audio/sound_effect/get"
  val SoundEffectEnabledGet  = "/api/audio/sound_effect/enabled/get"
  val SoundEffectEnabledSet  = "/api/audio/sound_effect/enabled/set"
  val SoundEffectAngleGet    = "/api/audio/sound_effect/angle/get"
  val SoundEffectAngleSet    = "/api/audio/sound_effect/angle/set"
  val SoundEffectRoomSizeGet = "/api/audio/sound_effect/room_size/get"
  val SoundEffectRoomSizeSet = "/api/audio/sound_effect/room_size/set"

  // Equalizer
  val EqualizerGet            = "/api/audio/equalizer/get"
  val EqualizerEnabledGet     = "/api/audio/equalizer/enabled/get"
  val EqualizerEnabledSet     = "/api/audio/equalizer/enabled/set"
  val EqualizerPresetsListGet = "/api/audio/equalizer/presets_list/get"
  val EqualizerPresetIdGet    = "/api/audio/equalizer/preset_id/get"
  val EqualizerPresetIdSet    = "/api/audio/equalizer/preset_id/set"
  val EqualizerPresetValueGet = "/api/audio/equalizer/preset_value/get"
  val EqualizerPresetValueSet = "/api/audio/equalizer/preset_value/set"

  // Active noise control (noise cancellation)
  val ANCEnableGet = "/api/audio/noise_cancellation/enabled/get"
  val ANCEnableSet = "/api/audio/noise_cancellation/enabled/set"

  val SpecificModeEnableGet = "/api/audio/specific_mode/enabled/get"
  val SpecificModeEnableSet = "/api/audio/specific_mode/enabled/set"


  /*************
   * Bluetooth *
   *************/

  val FriendlynameGet = "/api/bluetooth/friendlyname/get"
  val FriendlynameSet = "/api/bluetooth/friendlyname/set"


  /**********
   * System *
   **********/

  val BatteryGet      = "/api/system/battery/get"
  val BatteryLevelGet = "/api/system/battery_level/get"

  val ANCPhoneModeEnableGet = "/api/system/anc_phone_mode/enabled/get"
  val ANCPhoneModeEnableSet = "/api/system/anc_phone_mode/enabled/set"

  val AutoConnectionEnableGet = "/api/system/auto_connection/enabled/get"
  val AutoConnectionEnableSet = "/api/system/auto_connection/enabled/set"

  val AutoPowerOffGet = "/api/system/auto_power_off/get"
  val AutoPowerOffSet = "/api/system/auto_power_off/set"

  val AutoPowerOffPresetsListGet = "/api/system/auto_power_off/presets_list_get"

  val Calibration = "/api/system/calibrate"

  val HeadDetectionEnabledGet = "/api/system/head_detection/enabled/get"
  val HeadDetectionEnabledSet = "/api/system/head_detection/enabled/set"

  val DeviceTypeGet = "/api/system/device_type/get"


  /*********
   * Other *
   *********/

  val AppliVersionSet       = "/api/appli_version/set"
  val DownloadCheckStateGet = "/api/software/download_check_state/get"
  val DownloadSizeSet       = "/api/software/download_size/set"
  val VersionCheckingGet    = "/api/software/version_checking/get"
  val VersionGet            = "/api/software/version/get"
}
