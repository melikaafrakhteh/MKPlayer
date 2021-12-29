package com.afrakhteh.musicplayer.model.entity.audio


data class MusicsOfAPlayList(
        val playList: AllPlayListEntity,
        val musicList: List<MusicEntity>
)
