package com.afrakhteh.musicplayer.model.db

import androidx.room.*
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.entity.db.relations.PlayListWithMusics

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addANewPlayList(item: PlayListEntity)

    @Delete
    suspend fun deleteOnePlayList(item: PlayListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMusicToPlayList(item: AllMusicsEntity)

    @Delete
    suspend fun deleteMusicFromPlayList(item: AllMusicsEntity)

    @Query("SELECT * FROM PlayListEntity")
    suspend fun getAllPlayLists(): List<PlayListEntity>

    @Transaction
    @Query("SELECT * FROM PlayListEntity WHERE playListId = :playListId")
    suspend fun getPlayListWithMusics(playListId: Int): List<PlayListWithMusics>
}