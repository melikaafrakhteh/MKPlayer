package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.model.db.FavoriteDao
import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
        private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addToFave(item: FavoriteEntity) {
        return dao.addToFave(item)
    }

    override suspend fun deleteFromFave(path: String) {
        return dao.deleteFromFave(path)
    }

    override fun getAllFaveList(): Flow<List<FavoriteEntity>> {
        return dao.getAllFaveAudio()
    }
}