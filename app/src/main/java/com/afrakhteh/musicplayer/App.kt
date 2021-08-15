package com.afrakhteh.musicplayer

import android.app.Application
import com.afrakhteh.musicplayer.di.components.ApplicationComponent
import com.afrakhteh.musicplayer.di.components.DaggerApplicationComponent


class App : Application() {
//This initializes the ApplicationComponent field when the application first starts up.

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent
                .builder()
                .context(this)
                .build()
    }
}