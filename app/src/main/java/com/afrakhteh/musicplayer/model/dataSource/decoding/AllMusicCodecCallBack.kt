package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.afrakhteh.musicplayer.constant.Numerals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext

class AllMusicCodecCallBack(private var impl: AudioDecodingImpl) : MediaCodec.Callback(), CoroutineScope {

    private var job: Job = Job()

    private var mOutputEOS = false
    private var mInputEOS = false
    var decoded: Long = 0
    private var percent = 0

    private var queueType: Int = 0
    private var sampleTime = 0L
    private var result: Int = 0
    private var total = 0
    private var advanced = false
    private var maxresult = 0
    private var extractor: MediaExtractor = MediaExtractor()
    private var gains: ArrayList<Int> = ArrayList()

    override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
        if (mOutputEOS or mInputEOS) return
        if (impl.isCanceled()) {
            endOfStream(codec, index)
            return
        }
        try {
            val inputBuffer: ByteBuffer = codec.getInputBuffer(index) ?: return

            if (queueType == Numerals.QUEUE_INPUT_BUFFER_EFFECTIVE) {
                successfulInputBuffer(codec, index, inputBuffer)
            } else {
                failedInputBuffer(codec, index, inputBuffer)
            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        impl.onProcessingProgress(percent)
        Log.d("All", "percents    $percent")
    }

    private fun successfulInputBuffer(codec: MediaCodec, index: Int, inputBuffer: ByteBuffer) {
        do {
            result = extractor.readSampleData(inputBuffer, total)
            if (result >= 0) {
                total += result
                sampleTime = extractor.sampleTime
                advanced = extractor.advance()
                maxresult = Math.max(maxresult, result)
            }
        } while (result >= 0 && total < maxresult * 5 && advanced && inputBuffer.capacity() - inputBuffer.limit() > maxresult * 3)
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

    private fun failedInputBuffer(codec: MediaCodec, index: Int, inputBuffer: ByteBuffer) {
        result = extractor.readSampleData(inputBuffer, 0)
        decoded += result
        if (result >= 0) {
            sampleTime = extractor.sampleTime
            codec.queueInputBuffer(index, 0, result, sampleTime, 0)
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

    override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
        mOutputEOS =
                mOutputEOS or (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0)
        codec.releaseOutputBuffer(index, false)
        if (mOutputEOS) {
            if (impl.isCanceled()) {
                impl.onProcessingCancel()
            } else {
                impl.onProcessingProgress(100)
            }
            impl.onFinishProcessing(gains, impl.duration, index)
        }
        job.cancel()
    }

    override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
        impl.onError(e)
    }

    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {}

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job
}