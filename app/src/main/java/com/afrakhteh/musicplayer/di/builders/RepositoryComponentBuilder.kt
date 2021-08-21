package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerRepositoryComponent
import com.afrakhteh.musicplayer.di.components.RepositoryComponent

object RepositoryComponentBuilder :
    ComponentBuilder<RepositoryComponent>() {

    override fun provideInstance(): RepositoryComponent {
        return DaggerRepositoryComponent.builder()
            .applicationComponent(ApplicationComponentBuilder.getInstance())
            .build()
    }
}