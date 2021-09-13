package com.afrakhteh.musicplayer.model.dataSource

import android.util.Log
import com.afrakhteh.musicplayer.constant.Lists.SUPPORTED_EXT
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioDecodingImpl
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class AudioWaveDataSource {

    fun decodedAudio(path: String, impl: AudioDecodingImpl) {
        try {
            checkAudioFile(path)
            impl.decodeFile(path)
        } catch (e: Exception) {
            Log.e("audioTag", "error in readAudio catch $e")
        }
    }

    private fun checkAudioFile(path: String) {
        val file = File(path)
        if (!file.exists()) {
            throw FileNotFoundException("error in readAudio $path")
        }
        if (!SUPPORTED_EXT.contains(separateAudioFormat(file))) {
            throw IOException("error in readAudio: no format:${separateAudioFormat(file)}")
        }
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

}







