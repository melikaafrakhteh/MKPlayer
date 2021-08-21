package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerViewModelComponent
import com.afrakhteh.musicplayer.di.components.ViewModelComponent

object ViewModelComponentBuilder : ComponentBuilder<ViewModelComponent>() {
    override fun provideInstance(): ViewModelComponent {
        return DaggerViewModelComponent.builder()
            .repositoryComponent(RepositoryComponentBuilder.getInstance())
            .build()
    }
}