package com.afrakhteh.musicplayer.model.repository.playList

import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.entity.db.relations.PlayListWithMusics

interface PlayListRepository {
    suspend fun addANewPlayList(item: PlayListEntity)
    suspend fun deleteOnePlayList(id: Int)
    suspend fun getAllPlayLists(): List<PlayListEntity>
    suspend fun getPlayListWithMusics(playListId: Int): List<PlayListWithMusics>
    suspend fun decreasePlayListSize(playListId: Int)
}