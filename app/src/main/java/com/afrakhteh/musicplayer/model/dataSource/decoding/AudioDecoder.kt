package com.afrakhteh.musicplayer.model.dataSource.decoding

interface AudioDecoder {
    fun onError(exception: Exception)
    fun setAudioDataSource(path: String)
    fun setOnFinishDecoding(onFinishDecoding: (ArrayList<Int>) -> Unit)
    fun setOnFrameAmpsCounted(onFrameAmpsCounted: (Int) -> Unit)
    suspend fun startDecoding()
}