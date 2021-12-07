package com.afrakhteh.musicplayer.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFave(item: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE path = :path")
    suspend fun deleteFromFave(path: String)

    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFaveAudio(): Flow<List<FavoriteEntity>>
}