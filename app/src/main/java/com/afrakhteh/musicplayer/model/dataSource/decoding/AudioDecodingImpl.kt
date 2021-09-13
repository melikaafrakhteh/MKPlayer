package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class AudioDecodingImpl : AudioDecodingListener {

    private val callBack = AllMusicCodecCallBack(this)
    private lateinit var extractor: MediaExtractor
    private lateinit var decoder: MediaCodec
    private lateinit var oneFrameAmps: IntArray
    private var format: MediaFormat? = null

    private var sampleRate = 0
    private var channelCount = 0
    var duration: Long = 0
    val path: String = MusicEntity().path
    val totalSize: Int = path.length

    private fun calculateSamplesPerFrame(): Int {
        return (sampleRate / 4f).toInt()
    }

    private fun findFilesFirstAudio(numTracks: Int, path: String) {
        var i = 0
        while (i < numTracks) {
            format = extractor.getTrackFormat(i)
            if (requireNotNull(format).getString(MediaFormat.KEY_MIME)!!.startsWith("audio/")) {
                extractor.selectTrack(i)
                break
            }
            i++
        }
        if (i == numTracks || format == null) {
            throw IOException("decodeFile:  No audio track found in $path")
        }
    }

    fun decodeFile(path: String) {

        extractor = MediaExtractor()

        val file = File(path)
        val fis: FileInputStream
        try {
            fis = FileInputStream(file)
            val fd: FileDescriptor = fis.fd
            extractor.setDataSource(fd)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val numTracks = extractor.trackCount
        findFilesFirstAudio(numTracks, path)

        channelCount = requireNotNull(format).getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        sampleRate = requireNotNull(format).getInteger(MediaFormat.KEY_SAMPLE_RATE)
        duration = requireNotNull(format).getLong(MediaFormat.KEY_DURATION)
        oneFrameAmps = IntArray(calculateSamplesPerFrame() * channelCount)

        onStartProcessing(duration, channelCount, sampleRate)

        decoder.setCallback(AllMusicCodecCallBack(this))
        decoder.configure(format, null, null, 0)
        decoder.start()
    }


    override fun isCanceled(): Boolean {
        return false
    }

    override fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int) {
        val mimeType = requireNotNull(format).getString(MediaFormat.KEY_MIME)
        decoder = MediaCodec.createDecoderByType(requireNotNull(mimeType))

        Log.d("All", "start processing")
    }

    override fun onProcessingProgress(percent: Int) {

        var processingPercent = percent
        val curProgress = (100 * callBack.decoded / totalSize.toFloat()).toInt()
        if (curProgress != processingPercent) processingPercent = curProgress
        Log.d("All", "during processing  $processingPercent")
        Log.d("All", "during processing  total  $curProgress")
    }

    override fun onProcessingCancel() {
        stopCodec()
        Log.d("All", "onProcessingCancel ")
    }

    override fun onFinishProcessing(data: ArrayList<Int>, duration: Long, index: Int) {
        var gains: ArrayList<Int> = ArrayList()
        try {
            val outputBuffer: ByteBuffer? = decoder.getOutputBuffer(index)
            gains = outPutResult(data, outputBuffer)
            setMap(gains)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        Log.d("All", "array data$data")
        stopCodec()
    }

    private fun outPutResult(data: ArrayList<Int>, outputBuffer: ByteBuffer?): ArrayList<Int> {
        var frameIndex = 0
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

    private fun stopCodec() {
        decoder.stop()
        decoder.release()
        extractor.release()
    }

    private fun setMap(data: ArrayList<Int>) {
        val minValue = requireNotNull(data.minOrNull())
        val maxValue = requireNotNull(data.maxOrNull())
        val diff = maxValue - minValue
        val mappedData = data.map { items ->
            (((items - minValue) * 100f) / diff).toInt()
        }

        Log.d("All", "mapped " + mappedData.toString())
    }

    override fun onError(exception: Exception) {
        Log.e("Impl", exception.toString())
    }

}