package com.afrakhteh.musicplayer.di.components

import android.content.SharedPreferences
import com.afrakhteh.musicplayer.di.modules.SharedPrefModule
import com.afrakhteh.musicplayer.di.scopes.PlayerActivityScope
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManager
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import dagger.Component

@PlayerActivityScope
@Component(
    modules = [SharedPrefModule::class],
    dependencies = [ApplicationComponent::class,
        ViewModelComponent::class]

)
interface PlayerActivityComponent {
    fun exposeSharedPreference(): SharedPreferences
    fun exposeSharedPreferenceManagerImpl(): PreferenceManager
    fun exposeSharedPreferenceEditor(): SharedPreferences.Editor

    fun inject(playerActivity: PlayerActivity)
}