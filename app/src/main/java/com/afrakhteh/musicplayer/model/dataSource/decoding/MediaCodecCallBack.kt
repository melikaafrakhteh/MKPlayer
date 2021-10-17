package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.afrakhteh.musicplayer.util.AudioUtils
import com.afrakhteh.musicplayer.util.AudioUtils.extractAudioDetails
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.sqrt

class MediaCodecCallBack(
        private var isCanceled: () -> Boolean,
        private var onProcessingCancel: (decoder: MediaCodec) -> Unit,
        private var onProcessingProgress: (Int) -> Unit,
        private var onFinishProcessing: (ArrayList<Int>) -> Unit,
        private val format: MediaFormat,
        private val extractor: MediaExtractor
) : MediaCodec.Callback() {

    private var mOutputEOS = false
    private var mInputEOS = false
    private var decoded: Long = 0
    private var percent = 0

    private var sampleTime = 0L
    private var resultOfEncoding: Int = 0
    private var total = 0
    private var advanced = false
    private var maxresult = 0
    private var frameIndex = 0
    private var isCancel = false

    private val gains: ArrayList<Int> = ArrayList()

    private val TAG: String = "callBack"

    init {
        isCanceled = ::checkCancellation
        onProcessingProgress = ::checkProcessing
        onProcessingCancel = ::checkCancelingProcess
    }

    private fun checkCancelingProcess(decoder: MediaCodec) {
        Log.d(TAG, "progress canceled")
        stopCodec(decoder)
    }

    private fun checkProcessing(percent: Int) {
        if (percent == 100) {
            Log.d(TAG, "end")
        }
    }

    private fun checkCancellation(): Boolean = isCancel

    override fun onError(
            codec: MediaCodec,
            e: MediaCodec.CodecException
    ) {
        Log.d(TAG, e.toString())
    }

    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {}

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
        if (mOutputEOS or mInputEOS) return
        if (isCanceled.invoke()) {
            endOfStream(codec, index)
            isCancel = true
            return
        }
        isCancel = false
        try {
            val inputBuffer: ByteBuffer = codec.getInputBuffer(index) ?: return
            processInputBuffer(codec, index, inputBuffer)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        onProcessingProgress.invoke(onProcessingProgress(percent))
    }

    override fun onOutputBufferAvailable(
            codec: MediaCodec,
            index: Int,
            info: MediaCodec.BufferInfo
    ) {
        isCancel = false
        try {
            val outputBuffer: ByteBuffer? = codec.getOutputBuffer(index)
            gains.addAll(outPutResult(outputBuffer))

            mOutputEOS =
                    mOutputEOS or (isEndOfStream(info))
            codec.releaseOutputBuffer(index, false)

            if (mOutputEOS) {
                if (isCanceled.invoke()) {
                    onProcessingCancel.invoke(codec)
                } else {
                    onProcessingProgress.invoke(100)
                    onFinishProcessing.invoke(gains)
                }
                stopCodec(codec)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            throw e
        }
    }

    private fun processInputBuffer(
            codec: MediaCodec,
            index: Int,
            inputBuffer: ByteBuffer) {
        total = 0
        advanced = false
        maxresult = 0
        do {
            resultOfEncoding = extractor.readSampleData(inputBuffer, total)
            if (resultOfEncoding >= 0) {
                total += resultOfEncoding
                sampleTime = extractor.sampleTime
                advanced = extractor.advance()
                maxresult = max(maxresult, resultOfEncoding)
            }
        } while (checkEncodingResult(inputBuffer))
        decoded += total
        if (advanced) {
            codec.queueInputBuffer(index, 0, total, sampleTime, 0)
        } else {
            codec.queueInputBuffer(
                    index,
                    0,
                    0,
                    -1,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
            )
            mInputEOS = true
        }
    }

    private fun endOfStream(codec: MediaCodec, index: Int) {
        codec.queueInputBuffer(index, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
        mInputEOS = true
    }

    private fun checkEncodingResult(inputBuffer: ByteBuffer): Boolean {
        return (resultOfEncoding >= 0
                && total < maxresult * 5
                && advanced
                && inputBuffer.capacity() - inputBuffer.limit() > maxresult * 3)
    }

    private fun isEndOfStream(info: MediaCodec.BufferInfo): Boolean {
        return info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0
    }

    private fun stopCodec(decoder: MediaCodec) {
        decoder.stop()
        decoder.release()
        extractor.release()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun onProcessingProgress(percent: Int): Int {
        var processingPercent = percent
        val curProgress = (100 * decoded / AudioUtils.findTotalSize("").toFloat()).toInt()
        if (curProgress != processingPercent)
            processingPercent = curProgress

        return processingPercent
    }

    private fun outPutResult(outputBuffer: ByteBuffer?): ArrayList<Int> {

        val oneFrameAmps = extractAudioDetails(format).oneFrameAmps
        val channelCount = extractAudioDetails(format).channelCount
        val data: ArrayList<Int> = ArrayList()

        if (outputBuffer != null) {
            outputBuffer.rewind()
            outputBuffer.order(ByteOrder.LITTLE_ENDIAN)
            while (outputBuffer.remaining() > 0) {
                oneFrameAmps[frameIndex] = outputBuffer.short.toInt()
                frameIndex++
                if (frameIndex >= oneFrameAmps.size - 1) {

                    var gain = -1
                    var value = 0
                    for (j in oneFrameAmps.indices step channelCount) {
                        for (k in 0 until channelCount) {
                            value += oneFrameAmps[j + k]
                        }
                        value /= channelCount
                        if (gain < value) {
                            gain = value
                        }
                    }
                    data.add(sqrt(gain.toDouble()).toInt())
                    frameIndex = 0
                }
            }
        }
        return data
    }
}
