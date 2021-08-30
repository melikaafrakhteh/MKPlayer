package com.afrakhteh.musicplayer.model.dataSource

class AudioImpl : AudioDecodingListener {
    override fun isCanceled(): Boolean {
        return false
    }

    override fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int) {

    }

    override fun onProcessingProgress(percent: Int) {

    }

    override fun onProcessingCancel() {

    }

    override fun onFinishProcessing(data: ArrayList<Int>, duration: Long) {

    }

    override fun onError(exception: Exception) {

    }
}