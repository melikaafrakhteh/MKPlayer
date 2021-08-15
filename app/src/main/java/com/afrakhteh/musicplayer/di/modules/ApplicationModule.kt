package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import com.afrakhteh.musicplayer.model.repository.MusicRepository
import com.afrakhteh.musicplayer.model.repository.MusicRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideRepository(context: Context): MusicRepository = MusicRepositoryImpl(context)
}