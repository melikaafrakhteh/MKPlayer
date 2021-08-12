package com.afrakhteh.musicplayer.model.entity

data class MusicEntity(
        var musicName: String? = null,
        var musicPath: String = "",
        var musicSinger: String? = null,
        var musicIndex: Int = 0
) {
}