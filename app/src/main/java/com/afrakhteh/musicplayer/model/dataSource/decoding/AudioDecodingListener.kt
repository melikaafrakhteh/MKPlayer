package com.afrakhteh.musicplayer.model.dataSource.decoding

interface AudioDecodingListener {
    fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int)
    fun onError(exception: Exception)
}