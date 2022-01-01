package com.afrakhteh.musicplayer.model.db

import androidx.room.*
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.entity.db.relations.PlayListWithMusics

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addANewPlayList(item: PlayListEntity)

    @Query("DELETE FROM PlayListEntity WHERE playListId = :id")
    suspend fun deleteOnePlayList(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMusicToPlayList(item: AllMusicsEntity)

    @Query("DELETE FROM AllMusicsEntity WHERE path = :path ")
    suspend fun deleteMusicFromPlayList(path: String)

    @Query("SELECT * FROM PlayListEntity")
    suspend fun getAllPlayLists(): List<PlayListEntity>

    @Query("SELECT title FROM PlayListEntity")
    suspend fun getAllPlayListTitle(): List<String>

    @Query("UPDATE PlayListEntity SET size = (size + 1) WHERE playListId= :playListId")
    suspend fun increasePlayListSize(playListId: Int)

    @Transaction
    @Query("SELECT * FROM PlayListEntity WHERE playListId = :playListId")
    suspend fun getPlayListWithMusics(playListId: Int): List<PlayListWithMusics>
}