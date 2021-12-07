package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFave(item: AudioPrePareToPlay)
    suspend fun deleteFromFave(path: String)
    fun getAllFaveList(): Flow<List<AudioPrePareToPlay>>
}