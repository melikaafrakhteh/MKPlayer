package com.afrakhteh.musicplayer.di.components


import android.media.MediaMetadataRetriever
import com.afrakhteh.musicplayer.di.modules.MediaMetadataModule
import com.afrakhteh.musicplayer.di.modules.RepositoryModule
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import dagger.Component

@RepoScope
@Component(
        modules = [RepositoryModule::class,
            MediaMetadataModule::class],
        dependencies = [ApplicationComponent::class,
            DataBaseComponent::class]
)
interface RepositoryComponent {
    fun exposeRepository(): MusicRepository
    fun exposeAudioDetailRepository(): AudioDetailsRepository
    fun exposeMediaMetaData(): MediaMetadataRetriever
    fun exposeFaveRepository(): FavoriteRepository
}