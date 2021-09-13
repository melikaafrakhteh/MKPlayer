package com.afrakhteh.musicplayer.di.components


import com.afrakhteh.musicplayer.di.modules.RepositoryModule
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import dagger.Component

@RepoScope
@Component(
    modules = [RepositoryModule::class],
    dependencies = [ApplicationComponent::class]
)
interface RepositoryComponent {
    fun exposeRepository(): MusicRepository
}