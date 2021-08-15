package com.afrakhteh.musicplayer.di.components

import android.content.Context
import com.afrakhteh.musicplayer.di.modules.ApplicationModule
import com.afrakhteh.musicplayer.views.fragments.AllMusicFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(allMusicFragment: AllMusicFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationComponent
    }
}