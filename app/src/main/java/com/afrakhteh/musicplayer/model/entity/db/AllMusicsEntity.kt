package com.afrakhteh.musicplayer.model.entity.db

import androidx.room.Entity

@Entity(primaryKeys = ["musicId", "playListId"])
data class AllMusicsEntity(
        val musicId: Int,
        val path: String,
        val name: String,
        val artist: String,
        val playListId: Int
)
