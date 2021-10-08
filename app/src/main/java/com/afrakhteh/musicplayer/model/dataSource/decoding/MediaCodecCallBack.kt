package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.util.AudioUtils
import com.afrakhteh.musicplayer.util.AudioUtils.extractAudioDetails
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class MediaCodecCallBack(
        private val isCanceled: () -> Boolean,
        private val onProcessingCancel: () -> Unit,
        private val onProcessingProgress: (Int) -> Unit,
        private val onFinishProcessing: (ArrayList<Int>, Long) -> Unit,
        private val format: MediaFormat,
        private val extractor: MediaExtractor,
        private val path: String
) : MediaCodec.Callback() {

    private var mOutputEOS = false
    private var mInputEOS = false
    var decoded: Long = 0
    private var percent = 0

    private var sampleTime = 0L
    private var resultOfEncoding: Int = 0
    private var total = 0
    private var advanced = false
    private var maxresult = 0

    private lateinit var gains: ArrayList<Int>

    private val TAG: String = "callBack"

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
        if (mOutputEOS or mInputEOS) return
        if (isCanceled.invoke()) {
            endOfStream(codec, index)
            return
        }
        try {
            val inputBuffer: ByteBuffer = codec.getInputBuffer(index) ?: return

            val queueType: Int = 1
            if (queueType == Numerals.QUEUE_INPUT_BUFFER_EFFECTIVE) {
                successfulInputBuffer(codec, index, inputBuffer)
            } else {
                failedInputBuffer(codec, index, inputBuffer)
            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        onProcessingProgress.invoke(onProcessingProgress(percent))
    }

    private fun successfulInputBuffer(
            codec: MediaCodec,
            index: Int,
            inputBuffer: ByteBuffer) {

        do {
            resultOfEncoding = extractor.readSampleData(inputBuffer, total)
            if (resultOfEncoding >= 0) {
                total += resultOfEncoding
                sampleTime = extractor.sampleTime
                advanced = extractor.advance()
                maxresult = Math.max(maxresult, resultOfEncoding)
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

    private fun failedInputBuffer(
            codec: MediaCodec,
            index: Int,
            inputBuffer: ByteBuffer) {

        resultOfEncoding = extractor.readSampleData(inputBuffer, 0)
        decoded += resultOfEncoding
        if (resultOfEncoding >= 0) {
            sampleTime = extractor.sampleTime
            codec.queueInputBuffer(index, 0, resultOfEncoding, sampleTime, 0)
            extractor.advance()
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

    override fun onOutputBufferAvailable(
            codec: MediaCodec,
            index: Int,
            info: MediaCodec.BufferInfo
    ) {
        gains = ArrayList()
        try {
            val outputBuffer: ByteBuffer? = codec.getOutputBuffer(index)
            gains = outPutResult(outputBuffer)
            //       mappedData(gains)

            mOutputEOS =
                    mOutputEOS or (isEndOfStream(info))
            codec.releaseOutputBuffer(index, false)

            if (mOutputEOS) {
                if (isCanceled.invoke()) {
                    onProcessingCancel.invoke()
                } else {
                    onProcessingProgress.invoke(100)
                    onFinishProcessing.invoke(gains, extractAudioDetails(format).duration)
                }
                stopCodec(codec)
            }
        } catch (e: IllegalStateException) {
            e.message
        }
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
        // val curProgress = (100 * decoded / AudioUtils.findTotalSize(extractor).toFloat()).toInt()
        val curProgress = (100 * decoded / AudioUtils.findTotalSize(path).toFloat()).toInt()
        if (curProgress != processingPercent)
            processingPercent = curProgress

        return processingPercent
    }

    private fun outPutResult(outputBuffer: ByteBuffer?): ArrayList<Int> {
        var frameIndex = 0
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
        Log.d(TAG, "gains: $data")
        return data

    }

    override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
        Log.d(TAG, e.toString())
    }

    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {}
}


private fun mappedData(data: ArrayList<Int>) {
    val minValue = requireNotNull(data.minOrNull())
    val maxValue = requireNotNull(data.maxOrNull())
    val diff = maxValue - minValue
    val mappedData = data.map { items ->
        (((items - minValue) * 100f) / diff).toInt()
    }
    Log.d("TAG", "mapped $mappedData")
}