package com.afrakhteh.musicplayer.di.components

import android.app.Application
import android.content.Context
import com.afrakhteh.musicplayer.App
import com.afrakhteh.musicplayer.di.modules.ApplicationModule
import com.afrakhteh.musicplayer.di.modules.SharedPrefModule
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManagerImpl
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SharedPrefModule::class
])
interface ApplicationComponent : AndroidInjector<App> {

    fun exposeContext(): Context
    fun exposeSharedManagerImp(): PreferenceManagerImpl

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindApplication(app: Application): Builder

        fun build(): ApplicationComponent
    }
}