package com.afrakhteh.musicplayer.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFave(item: AudioPrePareToPlay)

    @Query("DELETE FROM Audio_table WHERE path = :path")
    suspend fun deleteFromFave(path: String)

    @Query("SELECT * FROM Audio_table")
    fun getAllFaveAudio(): Flow<List<AudioPrePareToPlay>>
}