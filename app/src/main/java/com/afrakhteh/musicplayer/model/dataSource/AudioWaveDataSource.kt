package com.afrakhteh.musicplayer.model.dataSource

import android.util.Log
import com.afrakhteh.musicplayer.constant.Lists.SUPPORTED_EXT
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioDecoderImpl
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AudioWaveDataSource(private val impl: AudioDecoderImpl) {

    suspend fun decodeAudio(path: String, data: (ArrayList<Int>) -> Unit) {
        try {
            val checkedFile = checkAudioFile(path)
            checkAudioFormat(checkedFile)

            impl.setAudioDataSource(path)
            impl.startDecoding()

            val dataResult = impl.decodingResult()
            Log.d("audioTag", "data source:  $dataResult")
            data(dataResult)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("audioTag", "error in readAudio catch $e")
        }
    }

    private fun checkAudioFile(path: String): File {
        val file = File(path)
        if (!file.exists()) {
            throw FileNotFoundException("error in readAudio $path")
        }
        return file
    }

    private fun separateAudioFormat(file: File): String {
        val name: String = file.name.toLowerCase(Locale.ROOT)
        val components = name.split("\\.".toRegex()).toTypedArray()
        val audioFormat = components[components.size - 1]
        if (components.size < 2) {
            throw IOException("error in readAudio: size < 2")
        }
        return audioFormat
    }

    private fun checkAudioFormat(file: File) {
        if (!SUPPORTED_EXT.contains(separateAudioFormat(file))) {
            throw IOException("error in readAudio: no format:${separateAudioFormat(file)}")
        }
    }
}







