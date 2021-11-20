package com.afrakhteh.musicplayer.views.presenter

import com.afrakhteh.musicplayer.views.services.AudioServiceViewInterface

interface ArtAlbumPresenter {
    fun setService(service: AudioServiceViewInterface)
    fun getAudioByteArray()
    fun onDestroy()
}