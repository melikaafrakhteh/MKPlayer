package com.afrakhteh.musicplayer.model.entity.audio

data class MusicEntity(
        var name: String? = null,
        var path: String = "",
        var artist: String? = null,
        var index: Int = 0
)