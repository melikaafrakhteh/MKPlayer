package com.afrakhteh.musicplayer.views.main.state

import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.util.SingleEvent

data class PlayListState(
        val playLists: List<PlayListEntity> = listOf(),
        val message: SingleEvent<String>? = null
)
