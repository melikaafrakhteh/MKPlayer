package com.afrakhteh.musicplayer.views.main.state

import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.util.SingleEvent

data class PlayListState(
        val playLists: List<AllPlayListEntity> = listOf(),
        val message: SingleEvent<String>? = null
)
