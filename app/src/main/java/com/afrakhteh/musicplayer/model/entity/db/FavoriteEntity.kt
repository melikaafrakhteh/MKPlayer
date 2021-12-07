package com.afrakhteh.musicplayer.model.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
        @PrimaryKey val id: Int,
        val path: String
)
