package com.afrakhteh.musicplayer.model.dataSource

interface AudioDecodingListener {

    fun isCanceled(): Boolean
    fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int)
    fun onProcessingProgress(percent: Int)
    fun onProcessingCancel()
    fun onFinishProcessing(data: ArrayList<Int>, duration: Long)
    fun onError(exception: Exception)
}