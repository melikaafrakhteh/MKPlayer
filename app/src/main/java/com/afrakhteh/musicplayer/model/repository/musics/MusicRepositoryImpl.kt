package com.afrakhteh.musicplayer.model.repository.musics


import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioArtPictureReadable
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import java.io.File
import javax.inject.Inject


@RepoScope
class MusicRepositoryImpl @Inject constructor(
        private val context: Context,
        private val metadataRetriever: MediaMetadataRetriever
) : MusicRepository {

    override suspend fun getAllMusic(): List<MusicEntity> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor = createQuerySample(selection, context) ?: return arrayListOf()
        return getMusicListFromCursor(cursor)
    }

    override suspend fun getRecentlyMusic(): List<MusicEntity> {
        val selection = MediaStore.Audio.Media.DATE_ADDED +
                ">" + (System.currentTimeMillis() / 1000 - (7 * 60 * 60 * 24))
        val cursor = createQuerySample(selection, context) ?: return arrayListOf()
        return getMusicListFromCursor(cursor)
    }

    override suspend fun getMusicArtPicture(path: String): ByteArray? {
        return AudioArtPictureReadable(metadataRetriever).read(path)
    }

    override suspend fun getMusicListById(idList: List<Int>): List<MusicEntity> {
        val selection = MediaStore.Audio.Media._ID + " IN (" +
                idList.joinToString(", ") + ")"

        val cursor = createQuerySample(selection, context) ?: return emptyList()
        return getMusicListFromCursor(cursor)
    }

    private fun createQuerySample(selection: String, context: Context): Cursor? {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media._ID
        )
        val order = "LOWER(" + MediaStore.Audio.Media.DATE_ADDED + ") DESC"
        return context.contentResolver.query(
                uri, projection, selection, null, order
        )
    }

    private fun getMusicListFromCursor(cursor: Cursor): List<MusicEntity> {
        val list: MutableList<MusicEntity> = arrayListOf()
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

            if (checkValidMusicPath(path)) list.add(audio)
        }
        cursor.close()
        return list
    }

    override suspend fun deleteItemFromList(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
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
