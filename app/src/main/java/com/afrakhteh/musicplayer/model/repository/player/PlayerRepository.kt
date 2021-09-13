package com.afrakhteh.musicplayer.model.repository.player

interface PlayerRepository {

    fun getAllMusicWaves(): List<Int>
}