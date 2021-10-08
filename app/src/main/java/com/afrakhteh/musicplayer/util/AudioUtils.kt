package com.afrakhteh.musicplayer.util

import android.media.MediaExtractor
import android.media.MediaFormat
import com.afrakhteh.musicplayer.model.entity.AudioDetails
import java.io.IOException

object AudioUtils {

    fun extractAudioDetails(format: MediaFormat?): AudioDetails {
        val channelCount = requireNotNull(format).getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        val sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        val duration = format.getLong(MediaFormat.KEY_DURATION)
        val oneFrameAmps = IntArray(calculateSamplesPerFrame(sampleRate) * channelCount)
        val mimeType = format.getString(MediaFormat.KEY_MIME)

        return AudioDetails(channelCount, sampleRate, duration, oneFrameAmps, mimeType)
    }


    private fun calculateSamplesPerFrame(sampleRate: Int): Int {
        return (sampleRate / 4f).toInt()
    }

    fun findFileFirstAudio(extractor: MediaExtractor, numTracks: Int): MediaFormat {
        var i = 0
        var format: MediaFormat? = null
        while (i < numTracks) {
            format = extractor.getTrackFormat(i)
            selectAudioTrack(i, format, extractor)
            break
            i++
        }

        if (i == numTracks || format == null) {
            throw IOException("decodeFile:  No audio track found")
        }
        return format
    }

    fun selectAudioTrack(i: Int, format: MediaFormat?, extractor: MediaExtractor) {
        if (requireNotNull(format).getString(MediaFormat.KEY_MIME)!!.startsWith("audio/")) {
            extractor.selectTrack(i)
        }
    }
/*    @RequiresApi(Build.VERSION_CODES.P)
    fun findTotalSize(extractor: MediaExtractor): Long {
        return extractor.sampleSize
    }*/

    fun findTotalSize(path: String): Int {
        return path.length
    }

}