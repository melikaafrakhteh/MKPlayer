package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import androidx.room.Room
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.di.scopes.DBScope
import com.afrakhteh.musicplayer.model.db.FavoriteDao
import com.afrakhteh.musicplayer.model.db.MusicDataBase
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @DBScope
    @Provides
    fun provideDataBase(context: Context): MusicDataBase {
        return Room.databaseBuilder(
                context,
                MusicDataBase::class.java,
                Strings.DATABASE_NAME
        ).build()
    }

    @DBScope
    @Provides
    fun provideDao(room: MusicDataBase): FavoriteDao {
        return room.faveDao()
    }
}