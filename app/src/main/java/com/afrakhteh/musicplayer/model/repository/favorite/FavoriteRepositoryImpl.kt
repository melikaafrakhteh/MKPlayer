package com.afrakhteh.musicplayer.model.repository.favorite

import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.db.FavoriteDao
import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import javax.inject.Inject

@RepoScope
class FavoriteRepositoryImpl @Inject constructor(
        private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addToFave(item: FavoriteEntity) {
        return dao.addToFave(item)
    }

    override suspend fun deleteFromFave(path: String) {
        return dao.deleteFromFave(path)
    }

    override suspend fun getAllFaveList(): List<FavoriteEntity> {
        return dao.getAllFaveAudio()
    }

    override suspend fun isMusicLiked(path: String): Boolean {
        return dao.isMusicLiked(path) != 0
    }
}