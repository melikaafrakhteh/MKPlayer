package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerPlayerComponent
import com.afrakhteh.musicplayer.di.components.PlayerComponent

object PlayerComponentBuilder :
        ComponentBuilder<PlayerComponent>() {

    override fun provideInstance(): PlayerComponent {
        return DaggerPlayerComponent.builder()
                .applicationComponent(ApplicationComponentBuilder.getInstance())
                .repositoryComponent(RepositoryComponentBuilder.getInstance())
                .build()
    }
}