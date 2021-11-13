package com.afrakhteh.musicplayer.model.repository.musics


import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioArtPictureReadable
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

    override suspend fun getMusicArtPicture(path: String): ByteArray? {
        return AudioArtPictureReadable(MediaMetadataRetriever()).read(path)
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
        return (path.endsWith(".aac", ignoreCase = true)
                || path.endsWith(".mp3", ignoreCase = true)
                || path.endsWith(".wav", ignoreCase = true)
                || path.endsWith(".ogg", ignoreCase = true)
                || path.endsWith(".ac3", ignoreCase = true)
                || path.endsWith(".mid", ignoreCase = true)
                || path.endsWith(".m4a", ignoreCase = true))
    }
}
