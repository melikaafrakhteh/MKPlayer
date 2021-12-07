package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity

interface FavoriteRepository {
    suspend fun addToFave(item: FavoriteEntity)
    suspend fun deleteFromFave(path: String)
    suspend fun getAllFaveList(): List<FavoriteEntity>
    suspend fun isMusicLiked(path: String): Boolean
}