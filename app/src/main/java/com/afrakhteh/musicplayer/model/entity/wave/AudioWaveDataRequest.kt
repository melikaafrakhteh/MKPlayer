package com.afrakhteh.musicplayer.model.entity.wave

import java.util.*

data class AudioWaveDataRequest(
        val path: String,
        val onDataPrepared: (ArrayList<Int>) -> Unit,
        val onFrameCounted: (Int) -> Unit
)
