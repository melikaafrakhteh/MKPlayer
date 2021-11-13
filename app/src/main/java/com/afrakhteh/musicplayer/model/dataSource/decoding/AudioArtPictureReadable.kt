package com.afrakhteh.musicplayer.model.dataSource.decoding

import android.media.MediaMetadataRetriever
import com.afrakhteh.musicplayer.dataSources.Readable
import javax.inject.Inject

class AudioArtPictureReadable @Inject constructor(
        private val mmr: MediaMetadataRetriever
) : Readable.IO<String, ByteArray?> {

    override fun read(input: String): ByteArray? {
        return try {
            mmr.setDataSource(input)
            mmr.embeddedPicture
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}