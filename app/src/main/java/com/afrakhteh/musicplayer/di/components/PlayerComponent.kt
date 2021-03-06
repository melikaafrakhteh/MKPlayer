package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.ExoPlayerModule
import com.afrakhteh.musicplayer.di.modules.PresenterModule
import com.afrakhteh.musicplayer.di.scopes.PlayerScope
import com.afrakhteh.musicplayer.views.services.AudioPlayerService
import dagger.Component

@PlayerScope
@Component(
        modules = [ExoPlayerModule::class,
            PresenterModule::class],
        dependencies = [ApplicationComponent::class,
            RepositoryComponent::class]
)
interface PlayerComponent {
    fun inject(playerService: AudioPlayerService)
}