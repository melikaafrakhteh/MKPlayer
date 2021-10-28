package com.afrakhteh.musicplayer.model.entity

data class AudioPrePareToPlay(
        val id: Int,
        val path: String,
        val album: String,
        val musicName: String,
        val musicArtist: String
)
