package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerSharedPrefComponent
import com.afrakhteh.musicplayer.di.components.SharedPrefComponent

object SharedPrefComponentBuilder : ComponentBuilder<SharedPrefComponent>() {
    override fun provideInstance(): SharedPrefComponent {
        return DaggerSharedPrefComponent.builder()
                .applicationComponent(ApplicationComponentBuilder.getInstance())
                .build()
    }
}