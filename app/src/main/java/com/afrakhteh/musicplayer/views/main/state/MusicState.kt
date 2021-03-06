package com.afrakhteh.musicplayer.views.main.state

import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.util.SingleEvent

data class MusicState(
        val musicItems: List<MusicEntity> = listOf(),
        val message: SingleEvent<String>? = null
)
