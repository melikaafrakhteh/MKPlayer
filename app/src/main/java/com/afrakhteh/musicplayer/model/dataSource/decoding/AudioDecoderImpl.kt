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
        private val extractor: MediaExtractor
) : AudioDecoder {

    private val TAG: String = "decoderImp"
    private lateinit var decoder: MediaCodec
    private lateinit var callBack: MediaCodecCallBack

    private val isCanceled: () -> Boolean = { false }
    private val onProcessingCancel: (decoder: MediaCodec) -> Unit = {}
    private val onProcessingProgress: (Int) -> Unit = {}
    private var onFinishProcessing: (ArrayList<Int>) -> Unit = { }
    private var onFrameAmpsCounted: (Int) -> Unit = {}


    override suspend fun startDecoding() {
        var format: MediaFormat? = null
        val numTracks = extractor.trackCount
        format = findFileFirstAudio(extractor, numTracks)
        val audioDetails = extractAudioDetails(format)

        onFrameAmpsCounted.invoke(audioDetails.oneFrameAmps.size)

        decoder = MediaCodec.createDecoderByType(requireNotNull(audioDetails.mimeType))

        callBack = MediaCodecCallBack(
                isCanceled,
                onProcessingCancel,
                onProcessingProgress,
                onFinishProcessing,
                format,
                extractor
        )
        decoder.setCallback(callBack)

        decoder.configure(format, null, null, 0)
        decoder.start()
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

    override fun setOnFinishDecoding(onFinishDecoding: (ArrayList<Int>) -> Unit) {
        onFinishProcessing = onFinishDecoding
    }

    override fun setOnFrameAmpsCounted(onFrameAmpsCounted: (Int) -> Unit) {
        this.onFrameAmpsCounted = onFrameAmpsCounted
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, exception.toString())
    }

}

