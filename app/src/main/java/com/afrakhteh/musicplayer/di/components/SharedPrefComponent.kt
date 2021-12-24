package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.SharedPrefModule
import com.afrakhteh.musicplayer.di.scopes.SharedScope
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManagerImpl
import dagger.Component

@SharedScope
@Component(
        modules = [SharedPrefModule::class],
        dependencies = [ApplicationComponent::class]
)
interface SharedPrefComponent {
    fun exposeSharedManagerImp(): PreferenceManagerImpl
}