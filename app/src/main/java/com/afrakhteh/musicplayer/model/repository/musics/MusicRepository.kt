package com.afrakhteh.musicplayer.model.repository.musics


import com.afrakhteh.musicplayer.model.entity.MusicEntity

interface MusicRepository {
    fun getAllMusic(): List<MusicEntity>
    suspend fun getMusicArtPicture(path: String): ByteArray?
}