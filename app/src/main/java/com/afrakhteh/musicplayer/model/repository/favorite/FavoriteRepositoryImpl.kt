package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.model.db.FavoriteDao
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
        private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addToFave(item: AudioPrePareToPlay) {
        return dao.addToFave(item)
    }

    override suspend fun deleteFromFave(path: String) {
        return dao.deleteFromFave(path)
    }

    override fun getAllFaveList(): Flow<List<AudioPrePareToPlay>> {
        return dao.getAllFaveAudio()
    }
}