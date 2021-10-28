package com.afrakhteh.musicplayer.views.services

import com.google.android.exoplayer2.Player

class ExoPlayerListener : Player.Listener {

    private var mExoSongStateCallback: OnMusicStateCallback? = null

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE,
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                AudioPlayerService().setCurrentSongState()
            }
            Player.STATE_ENDED -> {
                mExoSongStateCallback?.onCompletion()
            }
        }
    }

}