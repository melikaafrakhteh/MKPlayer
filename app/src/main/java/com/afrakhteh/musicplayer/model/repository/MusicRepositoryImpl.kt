package com.afrakhteh.musicplayer.model.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.afrakhteh.musicplayer.model.entity.MusicEntity


class MusicRepositoryImpl(val context: Context) : MusicRepository {

    override fun getAllMusic(): List<MusicEntity> {

        val tempAudioList: MutableList<MusicEntity> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf<String>(
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.Media._ID
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val order = "LOWER(" + MediaStore.Audio.Media.DATE_ADDED + ") DESC"

        val c: Cursor? = context.contentResolver.query(
            uri, projection, selection, null, order
        )

        if (c != null) {
            while (c.moveToNext()) {
                val audio = MusicEntity()
                val name = c.getString(0)
                val path = c.getString(1)
                val artist = c.getString(2)
                val index = c.getInt(3)

                audio.musicName = name
                audio.musicPath = path
                audio.musicSinger = artist
                audio.musicIndex - index

                if (path != null && (path.endsWith(".aac")
                            || path.endsWith(".mp3")
                            || path.endsWith(".wav")
                            || path.endsWith(".ogg")
                            || path.endsWith(".ac3")
                            || path.endsWith(".mid")
                            || path.endsWith(".m4a"))
                ) {

                    tempAudioList.add(audio)
                }
            }
            c.close()
        }

        return tempAudioList
    }
}
