package com.afrakhteh.musicplayer.views.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay

class AudioPlayerService : Service() {

    private var audioListToPlay: List<AudioPrePareToPlay> = emptyList()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun play() {}

    fun pause() {}

    fun playNext() {}

    fun playPrevious() {}

    fun changeRepeatType() {}

    fun setAudioList(audioPrePareToPlayList: List<AudioPrePareToPlay>) {
        audioListToPlay = audioPrePareToPlayList
    }

    private fun showNotification() {}

    private fun hideNotification() {}

    private fun handleIntent(intent: Intent) {}
}