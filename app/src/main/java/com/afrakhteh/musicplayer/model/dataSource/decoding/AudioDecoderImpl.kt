package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.afrakhteh.musicplayer.util.AudioUtils.extractAudioDetails
import com.afrakhteh.musicplayer.util.AudioUtils.findFileFirstAudio
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream

class AudioDecoderImpl(
        private val extractor: MediaExtractor,
        private val path: String
) : AudioDecodingListener {

    private val TAG: String = "decoderImp"
    private lateinit var decoder: MediaCodec
    private lateinit var callBack: MediaCodecCallBack

    val isCanceled: () -> Boolean = { false }
    val onProcessingCancel: () -> Unit = {}
    val onProcessingProgress: (Int) -> Unit = {}
    val onFinishProcessing: (ArrayList<Int>, Long) -> Unit =
            { data: ArrayList<Int>, duration: Long -> }


    suspend fun decodeFile() {
        var format: MediaFormat? = null
        val numTracks = extractor.trackCount
        format = findFileFirstAudio(extractor, numTracks)
        val audioDetails = extractAudioDetails(format)
        //       decoder = MediaCodec.createDecoderByType(extractMimeType(format))

        decoder = MediaCodec.createDecoderByType(requireNotNull(audioDetails.mimeType))

        callBack = MediaCodecCallBack(
                isCanceled,
                onProcessingCancel,
                onProcessingProgress,
                onFinishProcessing,
                format,
                extractor,
                path)
        decoder.setCallback(callBack)

        //   format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 655360)
        decoder.configure(format, null, null, 0)
        decoder.start()
    }

    fun setAudioDataSource(path: String) {
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

    override fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int) {

    }

    override fun onError(exception: Exception) {
        Log.e(TAG, exception.toString())
    }

}

