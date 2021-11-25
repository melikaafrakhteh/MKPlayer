package com.afrakhteh.musicplayer.model.dataSource

import com.afrakhteh.musicplayer.constant.Lists.SUPPORTED_EXT
import com.afrakhteh.musicplayer.dataSources.Readable
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioDecoderImpl
import com.afrakhteh.musicplayer.model.entity.wave.AudioWaveDataRequest
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AudioWaveReadable(
        private val audioDecoder: AudioDecoderImpl
) : Readable.Void.Suspendable.IO<AudioWaveDataRequest> {

    override suspend fun read(input: AudioWaveDataRequest) {
        try {
            val checkedFile = fetchAudioFile(input.path)
            checkAudioFormat(checkedFile)

            audioDecoder.setAudioDataSource(input.path)

            audioDecoder.setOnFinishDecoding { preparedData ->
                input.onDataPrepared(mappedData(preparedData))
        //        Log.d("audioWave","dataPrepare: ${mappedData(preparedData)}")
            }

            audioDecoder.setOnFrameAmpsCounted { size ->
                input.onFrameCounted(size)
                //        Log.d("audioWave","frame: ${size}")
            }

            audioDecoder.startDecoding()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun mappedData(gains: ArrayList<Int>): ArrayList<Int> {
        val minValue = requireNotNull(gains.minOrNull())
        val maxValue = requireNotNull(gains.maxOrNull())
        val diff = maxValue - minValue
        val mapped = gains.map { items ->
            (((items - minValue) * 100f) / diff).toInt()
        }
        return mapped as ArrayList<Int>
    }

    private fun fetchAudioFile(path: String): File {
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







