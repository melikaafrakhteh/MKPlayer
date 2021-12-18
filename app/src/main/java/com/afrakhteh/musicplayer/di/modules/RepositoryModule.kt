package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepositoryImpl
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepositoryImpl
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepositoryImpl
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

    @Binds
    fun bindFavoriteRepository(repo: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    fun bindPlayListRepository(repository: PlayListRepositoryImpl): PlayListRepository
}