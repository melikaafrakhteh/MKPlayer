package com.afrakhteh.musicplayer.model.repository


import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import javax.inject.Inject

@RepoScope
class MusicRepositoryImpl @Inject constructor(
        val context: Context
) : MusicRepository {

    override fun getAllMusic(): List<MusicEntity> {

        val tempAudioList: MutableList<MusicEntity> = ArrayList()
        val cursor = createQueryForAllMusic(context) ?: return tempAudioList

        while (cursor.moveToNext()) {
            val audio = MusicEntity()
            val name = cursor.getString(0)
            val path = cursor.getString(1)
            val artist = cursor.getString(2)
            val index = cursor.getInt(3)

            audio.name = name
            audio.path = path
            audio.artist = artist
            audio.index = index

            if (checkValidMusicPath(path)) tempAudioList.add(audio)
        }
        cursor.close()
        return tempAudioList
    }

    private fun createQueryForAllMusic(context: Context): Cursor? {

        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf<String>(
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media._ID
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val order = "LOWER(" + MediaStore.Audio.Media.DATE_ADDED + ") DESC"

        return context.contentResolver.query(
                uri, projection, selection, null, order
        )
    }

    private fun checkValidMusicPath(path: String): Boolean {
        return (path.endsWith(".aac")
                || path.endsWith(".mp3")
                || path.endsWith(".wav")
                || path.endsWith(".ogg")
                || path.endsWith(".ac3")
                || path.endsWith(".mid")
                || path.endsWith(".m4a"))
    }
}
