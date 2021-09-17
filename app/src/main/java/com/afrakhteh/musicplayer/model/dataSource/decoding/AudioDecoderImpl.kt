package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.afrakhteh.musicplayer.util.AudioUtils.extractAudioDetails
import com.afrakhteh.musicplayer.util.AudioUtils.findFileFirstAudio
import com.afrakhteh.musicplayer.util.AudioUtils.selectAudioTrack
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream

class AudioDecoderImpl(
        private val extractor: MediaExtractor,
) : AudioDecodingListener {

    private val TAG: String = "decoderImp"
    private lateinit var decoder: MediaCodec

    val isCanceled: () -> Boolean = { false }
    val onProcessingCancel: () -> Unit = {}
    val onProcessingProgress: (Int) -> Unit = {}


    fun decodeFile() {
        val format: MediaFormat? = null
        val numTracks = extractor.trackCount
        val index = findFileFirstAudio(extractor, numTracks)
        selectAudioTrack(index, format, extractor)

        val audioDetails = extractAudioDetails(format)
        decoder = MediaCodec.createDecoderByType(requireNotNull(audioDetails.mimeType))

        decoder.setCallback(AllMusicCodecCallBack(this,
                this::isCanceled,
                this::onProcessingCancel,
                this::onProcessingProgress))
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

    override fun onFinishProcessing(data: ArrayList<Int>, duration: Long, index: Int) {

    }

    override fun onError(exception: Exception) {
        Log.e(TAG, exception.toString())
    }

    /*   override fun onProcessingProgress(percent: Int) {
           var processingPercent = percent
           val curProgress = (100 * callBack.decoded / findTotalSize(extractor).toFloat()).toInt()
           if (curProgress != processingPercent) processingPercent = curProgress
           Log.d("All", "during processing  $processingPercent")
           Log.d("All", "during processing  total  $curProgress")
       }*/

    /*   override fun onProcessingCancel() {
           stopCodec()
           Log.d("All", "onProcessingCancel ")
       */
}

/*   override fun onFinishProcessing(data: ArrayList<Int>, duration: Long, index: Int) {
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

   private fun mappedData(data: ArrayList<Int>) {
       val minValue = requireNotNull(data.minOrNull())
       val maxValue = requireNotNull(data.maxOrNull())
       val diff = maxValue - minValue
       val mappedData = data.map { items ->
           (((items - minValue) * 100f) / diff).toInt()
       }

       Log.d("All", "mapped $mappedData")
   }

   override fun onError(exception: Exception) {
       Log.e("Impl", exception.toString())
   }
*/
