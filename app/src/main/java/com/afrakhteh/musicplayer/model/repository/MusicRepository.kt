package com.afrakhteh.musicplayer.model.repository


import com.afrakhteh.musicplayer.model.entity.MusicEntity

interface MusicRepository {
    fun getAllMusic(): List<MusicEntity>
}