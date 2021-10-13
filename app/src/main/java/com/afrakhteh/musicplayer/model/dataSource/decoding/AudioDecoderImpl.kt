package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.afrakhteh.musicplayer.util.AudioUtils.extractAudioDetails
import com.afrakhteh.musicplayer.util.AudioUtils.findFileFirstAudio
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream

class AudioDecoderImpl(
        private val extractor: MediaExtractor,
        private val path: String
) : AudioDecoder {

    private val TAG: String = "decoderImp"
    private lateinit var decoder: MediaCodec
    private lateinit var callBack: MediaCodecCallBack

    val result: ArrayList<Int> = ArrayList()


    private val isCanceled: () -> Boolean = { false }
    private val onProcessingCancel: (decoder: MediaCodec) -> Unit = {}
    private val onProcessingProgress: (Int) -> Unit = {}
    private val onFinishProcessing: () -> ArrayList<Int> = { ArrayList() }

    override suspend fun startDecoding() {
        var format: MediaFormat? = null
        val numTracks = extractor.trackCount
        format = findFileFirstAudio(extractor, numTracks)
        val audioDetails = extractAudioDetails(format)

        decoder = MediaCodec.createDecoderByType(requireNotNull(audioDetails.mimeType))

        callBack = MediaCodecCallBack(
            isCanceled,
            onProcessingCancel,
            onProcessingProgress,
            onFinishProcessing,
            format,
            extractor,
            path
        )
        decoder.setCallback(callBack)

        decoder.configure(format, null, null, 0)
        decoder.start()
    }

    private fun processTime(): Long {
        return callBack.findProcessingTime(100)
    }

    override suspend fun decodingResult(): ArrayList<Int> {
        delay(processTime())
        result.addAll(callBack.gains)
        return result
    }

    override fun setAudioDataSource(path: String) {
        val file = File(path)
        val fis: FileInputStream
        try {
            fis = FileInputStream(file)
            val fd: FileDescriptor = fis.fd
            extractor.setDataSource(fd)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, exception.toString())
    }

}

