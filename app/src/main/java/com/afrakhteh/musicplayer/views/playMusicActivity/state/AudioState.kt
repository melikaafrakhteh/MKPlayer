package com.afrakhteh.musicplayer.views.playMusicActivity.state

import com.afrakhteh.musicplayer.constant.Numerals

class AudioState(state: Int) {

    var currentState = NONE_STATE

    init {
        currentState = state
    }

    companion object {
        const val NONE_STATE = Numerals.NONE_STATE_VALUE
        const val PLAY_STATE = Numerals.PLAY_STATE_VALUE
        const val PAUSE_STATE = Numerals.PAUSE_STATE_VALUE
        const val BUFFERING_STATE = Numerals.BUFFERING_STATE_VALUE
        const val STOPPED_STATE = Numerals.STOPPED_STATE_VALUE
    }
}