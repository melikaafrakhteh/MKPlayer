package com.afrakhteh.musicplayer.model.entity

data class AudioDetails(
        val channelCount: Int,
        val sampleRate: Int,
        val duration: Long,
        val oneFrameAmps: ArrayList<Int>,
        val mimeType: String?
)
