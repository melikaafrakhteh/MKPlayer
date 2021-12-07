package com.afrakhteh.musicplayer.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity

@Database(
        entities = [FavoriteEntity::class],
        version = Numerals.DATABASE_VERSION,
        exportSchema = false
)
abstract class MusicDataBase : RoomDatabase() {
    abstract fun faveDao(): FavoriteDao
}