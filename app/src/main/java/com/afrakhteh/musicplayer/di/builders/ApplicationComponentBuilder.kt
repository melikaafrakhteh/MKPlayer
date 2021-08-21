package com.afrakhteh.musicplayer.di.builders

import android.app.Application
import com.afrakhteh.musicplayer.di.components.ApplicationComponent
import com.afrakhteh.musicplayer.di.components.DaggerApplicationComponent

object ApplicationComponentBuilder :
    ComponentBuilder<ApplicationComponent>() {

    lateinit var app: Application

    override fun provideInstance(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .bindApplication(app)
            .build()
    }
}