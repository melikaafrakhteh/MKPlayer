package com.afrakhteh.musicplayer.views.services

interface OnMusicStateCallback {
    fun onPlaybackStatusChanged(state: Int)
    fun onCompletion()
}