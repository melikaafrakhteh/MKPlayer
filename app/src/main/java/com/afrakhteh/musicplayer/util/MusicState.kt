package com.afrakhteh.musicplayer.util

import com.afrakhteh.musicplayer.model.entity.MusicEntity

data class MusicState(
    val musicItems: List<MusicEntity> = listOf(),
    val playingMusicIndex: Int = 0
)
