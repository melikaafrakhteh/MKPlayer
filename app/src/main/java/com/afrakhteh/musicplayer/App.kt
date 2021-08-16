package com.afrakhteh.musicplayer

import android.app.Application
import com.afrakhteh.musicplayer.di.builders.ApplicationComponentBuilder



class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationComponentBuilder.app = this
    }
}