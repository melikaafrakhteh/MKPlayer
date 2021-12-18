package com.afrakhteh.musicplayer.model.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AllMusicsEntity(
        @PrimaryKey
        val musicId: Int,
        val path: String,
        val name: String,
        val artist: String,
        val playListId: Int
)
