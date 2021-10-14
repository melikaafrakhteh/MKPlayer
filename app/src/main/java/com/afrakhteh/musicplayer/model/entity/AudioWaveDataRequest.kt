package com.afrakhteh.musicplayer.model.entity

import java.util.*

data class AudioWaveDataRequest(
        val path: String,
        val onDataPrepared: (ArrayList<Int>) -> Unit
)
