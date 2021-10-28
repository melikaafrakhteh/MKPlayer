package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.ExoPlayerModule
import com.afrakhteh.musicplayer.di.scopes.PlayerScope
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Component

@PlayerScope
@Component(
        modules = [ExoPlayerModule::class],
        dependencies = [ApplicationComponent::class]
)
interface PlayerComponent {
    fun injectPlayer(exo: SimpleExoPlayer)
}