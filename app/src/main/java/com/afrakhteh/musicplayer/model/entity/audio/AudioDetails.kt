package com.afrakhteh.musicplayer.model.entity.audio

data class AudioDetails(
        val channelCount: Int,
        val sampleRate: Int,
        val duration: Long,
        val oneFrameAmps: IntArray,
        val mimeType: String?
)
