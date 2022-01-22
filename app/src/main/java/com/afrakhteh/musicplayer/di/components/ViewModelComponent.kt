package com.afrakhteh.musicplayer.di.components

import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.modules.ViewModelModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.viewModel.PlayerViewModel
import com.afrakhteh.musicplayer.views.main.fragments.*
import dagger.Component

@ViewModelScope
@Component(
    modules = [ViewModelFactoryModule::class,
        ViewModelModule::class],
    dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {
    fun exposePlayerViewModel(): PlayerViewModel
    fun exposeViewModelFactory(): ViewModelProvider.Factory

    fun inject(allMusicFragment: AllMusicFragment)
    fun inject(recentlyFragment: RecentlyFragment)
    fun inject(likedFragment: LikedFragment)
    fun inject(playListFragment: PlayListFragment)
    fun inject(playListMusicsFragment: PlayListMusicsFragment)
}