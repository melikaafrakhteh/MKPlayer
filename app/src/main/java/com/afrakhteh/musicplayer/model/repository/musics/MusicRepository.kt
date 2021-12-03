package com.afrakhteh.musicplayer.model.repository.musics


import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity

interface MusicRepository {
    suspend fun getAllMusic(): List<MusicEntity>
    suspend fun getRecentlyMusic(): List<MusicEntity>
    suspend fun getMusicArtPicture(path: String): ByteArray?
}