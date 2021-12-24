package com.afrakhteh.musicplayer.model.sharedPrefrences

interface PreferenceManager {
    fun readVolumeSharedPref(key: String): Float
    fun writeVolumeSharedPref(percent: Float)
}