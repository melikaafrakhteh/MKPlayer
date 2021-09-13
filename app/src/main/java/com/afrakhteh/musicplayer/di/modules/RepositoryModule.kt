package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindRepository(repo: MusicRepositoryImpl): MusicRepository
}