package com.afrakhteh.musicplayer.views.state

import com.afrakhteh.musicplayer.model.entity.MusicEntity

data class MusicState(
        val musicItems: List<MusicEntity> = listOf()
)
