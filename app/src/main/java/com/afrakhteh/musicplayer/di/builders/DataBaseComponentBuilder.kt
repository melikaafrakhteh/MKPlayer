package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerDataBaseComponent
import com.afrakhteh.musicplayer.di.components.DataBaseComponent

object DataBaseComponentBuilder : ComponentBuilder<DataBaseComponent>() {
    override fun provideInstance(): DataBaseComponent {
        return DaggerDataBaseComponent.builder()
                .applicationComponent(ApplicationComponentBuilder.getInstance())
                .build()
    }
}