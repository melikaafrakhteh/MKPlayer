package com.afrakhteh.musicplayer.model.repository.playList

import com.afrakhteh.musicplayer.model.db.PlayListDao
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.entity.db.relations.PlayListWithMusics
import javax.inject.Inject

class PlayListRepositoryImpl @Inject constructor(
        private val dao: PlayListDao
) : PlayListRepository {
    override suspend fun addANewPlayList(item: PlayListEntity) {
        dao.addANewPlayList(item)
    }

    override suspend fun deleteOnePlayList(item: PlayListEntity) {
        dao.deleteOnePlayList(item)
    }

    override suspend fun getAllPlayLists(): List<PlayListEntity> {
        return dao.getAllPlayLists()
    }

    override suspend fun getPlayListWithMusics(playListId: Int): List<PlayListWithMusics> {
        return dao.getPlayListWithMusics(playListId)
    }

}