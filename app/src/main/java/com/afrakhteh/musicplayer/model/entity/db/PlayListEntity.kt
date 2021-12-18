package com.afrakhteh.musicplayer.model.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayListEntity(
        @PrimaryKey(autoGenerate = false)
        val playListId: Int,
        val title: String
)
