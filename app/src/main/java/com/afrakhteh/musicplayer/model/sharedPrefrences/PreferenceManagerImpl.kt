package com.afrakhteh.musicplayer.model.sharedPrefrences

import android.content.SharedPreferences
import com.afrakhteh.musicplayer.constant.Strings
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : PreferenceManager {

    override fun readVolumeSharedPref(key: String): Float {
        return sharedPreferences.getFloat(Strings.VOLUME_SHARED_KEY, -1f)
    }

    override fun writeVolumeSharedPref(percent: Float) {
        editor.apply {
            clear()
            putFloat(Strings.VOLUME_SHARED_KEY, percent)
            apply()
        }
    }
}