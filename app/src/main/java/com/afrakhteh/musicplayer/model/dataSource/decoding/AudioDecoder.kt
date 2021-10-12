package com.afrakhteh.musicplayer.model.dataSource.decoding

interface AudioDecoder {
    fun onError(exception: Exception)
    fun setAudioDataSource(path: String)
    suspend fun startDecoding()
    suspend fun decodingResult(): ArrayList<Int>
}