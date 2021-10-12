package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.modules.ViewModelModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.views.mainActivity.fragments.AllMusicFragment
import com.afrakhteh.musicplayer.views.playMusicActivity.PlayerActivity
import dagger.Component

@ViewModelScope
@Component(
    modules = [ViewModelFactoryModule::class,
        ViewModelModule::class],
    dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {
    fun inject(allMusicFragment: AllMusicFragment)
    fun injectPlayer(playerActivity: PlayerActivity)
}