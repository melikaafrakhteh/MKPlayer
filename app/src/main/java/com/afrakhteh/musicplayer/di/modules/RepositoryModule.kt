package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepositoryImpl
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindRepository(repo: MusicRepositoryImpl): MusicRepository

    @Binds
    fun bindAudioDetailRepository(repo: AudioDetailsRepositoryImpl): AudioDetailsRepository
}