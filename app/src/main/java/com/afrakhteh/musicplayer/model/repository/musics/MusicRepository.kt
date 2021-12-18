package com.afrakhteh.musicplayer.model.repository.musics


import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity

interface MusicRepository {
    suspend fun getAllMusic(): List<MusicEntity>
    suspend fun getRecentlyMusic(): List<MusicEntity>
    suspend fun getMusicArtPicture(path: String): ByteArray?
    suspend fun getMusicListById(idList: List<Int>): List<MusicEntity>
    suspend fun deleteItemFromList(path: String)
    suspend fun addMusicToPlayList(item: AllMusicsEntity)
    suspend fun removeMusicFromPlayList(item: AllMusicsEntity)
}