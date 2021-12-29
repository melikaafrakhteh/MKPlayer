package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.modules.ViewModelModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.views.main.fragments.*
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import dagger.Component

@ViewModelScope
@Component(
        modules = [ViewModelFactoryModule::class,
            ViewModelModule::class],
        dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {

    fun inject(allMusicFragment: AllMusicFragment)
    fun inject(playerActivity: PlayerActivity)
    fun inject(recentlyFragment: RecentlyFragment)
    fun inject(likedFragment: LikedFragment)
    fun inject(playListFragment: PlayListFragment)
    fun inject(playListMusicsFragment: PlayListMusicsFragment)
}