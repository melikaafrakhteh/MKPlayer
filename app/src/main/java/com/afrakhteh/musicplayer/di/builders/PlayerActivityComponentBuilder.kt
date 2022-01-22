package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerPlayerActivityComponent
import com.afrakhteh.musicplayer.di.components.PlayerActivityComponent
import com.afrakhteh.musicplayer.di.modules.SharedPrefModule

object PlayerActivityComponentBuilder : ComponentBuilder<PlayerActivityComponent>() {
    override fun provideInstance(): PlayerActivityComponent {
        return DaggerPlayerActivityComponent.builder()
            .applicationComponent(ApplicationComponentBuilder.getInstance())
            .viewModelComponent(ViewModelComponentBuilder.getInstance())
            .sharedPrefModule(SharedPrefModule())
            .build()
    }
}