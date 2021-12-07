package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFave(item: FavoriteEntity)
    suspend fun deleteFromFave(path: String)
    fun getAllFaveList(): Flow<List<FavoriteEntity>>
}