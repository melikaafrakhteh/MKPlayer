package com.afrakhteh.musicplayer.views.services

import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay

interface AudioServiceViewInterface {
    fun getPlayingPosition(): Int?
    fun getMusicList(): List<AudioPrePareToPlay>
    fun setAudioByteArray(byteArray: ByteArray?)
}