package com.afrakhteh.musicplayer.model.dataSource

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Math.sqrt
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.collections.ArrayList


class AudioWaveDataSource {

    companion object {
        val SUPPORTED_EXT =
                listOf("mp3", "wav", "3gpp", "3gp", "amr", "aac", "m4a", "ogg")

        private const val SHORT_RECORD_DP_PER_SECOND = 25f
        private const val QUEUE_INPUT_BUFFER_EFFECTIVE = 1

        const val WAVEFORM_WIDTH = 1.5f
        const val LONG_RECORD_THRESHOLD_SECONDS = 20
    }

    private lateinit var extractor: MediaExtractor

    //  lateinit var format: MediaFormat
    var format: MediaFormat? = null
    private lateinit var decoder: MediaCodec
    private lateinit var gains: ArrayList<Int>
    private var sampleRate = 0
    private var channelCount = 0
    private lateinit var oneFrameAmps: IntArray
    private var frameIndex = 0
    private var duration: Long = 0

    fun readAudio(path: String, decodingListener: AudioDecodingListener) {
        try {
            val file = File(path)
            if (!file.exists()) {
                throw FileNotFoundException("error in readAudio $path")
            }
            val name: String = file.name.toLowerCase(Locale.ROOT)
            val components = name.split("\\.".toRegex()).toTypedArray()
            val audioFormat = components[components.size - 1]
            if (components.size < 2) {
                throw IOException("error in readAudio: size < 2")
            }
            if (!SUPPORTED_EXT.contains(audioFormat)) {
                throw IOException("error in readAudio: no format:$audioFormat")
            }

            decodeFile(path, decodingListener, QUEUE_INPUT_BUFFER_EFFECTIVE)

        } catch (e: Exception) {
            decodingListener.onError(e)
            Log.e("audioTag", "error in readAudio catch $e")
        }
    }

    fun audioSampleRate(input: File): ArrayList<Int> {
        var temp: ArrayList<Int>? = null
        try {
            if (!input.exists()) {
                throw  FileNotFoundException(input.absolutePath)
            }

            val extractor = MediaExtractor()
            // var format: MediaFormat? = null

            extractor.setDataSource(input.path)
            val numTracks = extractor.trackCount

            // find and select the first audio track present in the file.
            var i: Int = 0
            while (i < numTracks) {
                format = extractor.getTrackFormat(i)
                try {
                    if (format!!.getString(MediaFormat.KEY_MIME)!!.startsWith("audio/")) {
                        extractor.selectTrack(i)
                        break
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                i++
            }
            if (i == numTracks || format == null) {
                throw  IOException("No audio track found in " + input.path.toString())
            }

            try {
                format!!.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                format!!.getLong(MediaFormat.KEY_DURATION)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("All Music", "format is null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("All Music", "rate is null")
        }

        for (i in 0..duration) {
            temp = arrayListOf(sampleRate)
        }

        return temp!!
    }

    fun calculateSamplesPerFrame(): Int {
        return (sampleRate / 4f).toInt()
    }

    private fun decodeFile(path: String, decodeListener: AudioDecodingListener, queueType: Int) {
        gains = ArrayList()
        extractor = MediaExtractor()
        //ToDo fix  failed to instantiate extractor
        val file = File(path)
        var fis: FileInputStream
        try {
            fis = FileInputStream(file)
            val fd = fis.fd
            extractor.setDataSource(fd)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val numTracks = extractor.trackCount
        // find and select the first audio track present in the file.
        var i = 0
        while (i < numTracks) {
            format = extractor.getTrackFormat(i)
            if (format!!.getString(MediaFormat.KEY_MIME)!!.startsWith("audio/")) {
                extractor.selectTrack(i)
                break
            }
            i++
        }
        if (i == numTracks || format == null) {
            throw IOException("decodeFile:  No audio track found in $path")
        }

        channelCount = format!!.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        sampleRate = format!!.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        duration = format!!.getLong(MediaFormat.KEY_DURATION)

        // Make waveform independent from dpPerSec!!!
        // dpPerSec = getDpPerSecond(duration.toFloat() / 1000000f, context)
        oneFrameAmps = IntArray(calculateSamplesPerFrame() * channelCount)

        val mimeType = format!!.getString(MediaFormat.KEY_MIME)

        //Start decoding
        decoder = MediaCodec.createDecoderByType(mimeType!!)
        decodeListener.onStartProcessing(duration, channelCount, sampleRate)
        decoder.setCallback(object : MediaCodec.Callback() {

            private var mOutputEOS = false
            private var mInputEOS = false
            private var decoded: Long = 0
            private val totalSize: Int = path.length
            private var percent = 0

            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                if (mOutputEOS or mInputEOS) return
                if (decodeListener.isCanceled()) {
                    codec.queueInputBuffer(index, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                    mInputEOS = true
                    return
                }
                try {
                    val inputBuffer: ByteBuffer = codec.getInputBuffer(index) ?: return
                    var sampleTime = 0L
                    var result: Int
                    if (queueType == QUEUE_INPUT_BUFFER_EFFECTIVE) {
                        var total = 0
                        var advanced = false
                        var maxresult = 0
                        do {
                            result = extractor.readSampleData(inputBuffer, total)
                            if (result >= 0) {
                                total += result
                                sampleTime = extractor.sampleTime
                                advanced = extractor.advance()
                                maxresult = Math.max(maxresult, result)
                            }
                            //3 it is just for insurance. When remove it crash happens. it is ok if replace it by 2 number.
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
                    } else {
                        //If QUEUE_INPUT_BUFFER_EFFECTIVE failed then trying this way.
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
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
                val curProgress = (100 * decoded / totalSize.toFloat()).toInt()
                if (curProgress != percent) {
                    percent = curProgress
                    decodeListener.onProcessingProgress(percent)
                }

            }

            override fun onOutputBufferAvailable(
                    codec: MediaCodec,
                    index: Int,
                    info: MediaCodec.BufferInfo
            ) {
                try {
                    val outputBuffer: ByteBuffer? = codec.getOutputBuffer(index)
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
                                gains.add(sqrt(gain.toDouble()).toInt())
                                frameIndex = 0
                            }
                        }
                    }
                    mOutputEOS =
                            mOutputEOS or (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM !== 0)
                    codec.releaseOutputBuffer(index, false)
                    if (mOutputEOS) {
                        if (decodeListener.isCanceled()) {
                            decodeListener.onProcessingCancel()
                        } else {
                            decodeListener.onProcessingProgress(100)
                            decodeListener.onFinishProcessing(gains, duration)
                        }
                        codec.stop()
                        codec.release()
                        extractor.release()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
                decoder.configure(format, null, null, 0)
                decoder.start()
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                decodeListener.onError(e)
                Log.e("All Music", "onError")

            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {}
        })
    }

}







