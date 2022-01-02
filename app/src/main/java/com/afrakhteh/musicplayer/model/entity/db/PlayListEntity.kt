package com.afrakhteh.musicplayer.model.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayListEntity(
        @PrimaryKey(autoGenerate = true)
        val playListId: Int = 0,
        val title: String,
        val size: Int = 0
)
