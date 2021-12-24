package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerUseCaseComponent
import com.afrakhteh.musicplayer.di.components.UseCaseComponent

object UseCaseComponentBuilder : ComponentBuilder<UseCaseComponent>() {
    override fun provideInstance(): UseCaseComponent {
        return DaggerUseCaseComponent.builder()
                .repositoryComponent(RepositoryComponentBuilder.getInstance())
                .build()
    }
}