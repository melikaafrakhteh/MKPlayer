package com.afrakhteh.musicplayer.views.mainActivity.interfaces

import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.MusicEntity

interface MusicPlayer {
    fun play(data: MusicEntity, list: List<AudioPrePareToPlay>)
    fun pause()
    fun stop()
}