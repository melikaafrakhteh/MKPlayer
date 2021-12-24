package com.afrakhteh.musicplayer.model.sharedPrefrences

import android.content.Context
import android.content.SharedPreferences
import com.afrakhteh.musicplayer.constant.Strings
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(
        context: Context
) : PreferenceManager {

    private var sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                    Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE
            )

    override fun readVolumeSharedPref(key: String): Float {
        return sharedPreferences.getFloat(Strings.VOLUME_SHARED_KEY, -1f)
    }

    override fun writeVolumeSharedPref(percent: Float) {
        sharedPreferences.edit().putFloat(Strings.VOLUME_SHARED_KEY, percent)?.apply()
    }
}