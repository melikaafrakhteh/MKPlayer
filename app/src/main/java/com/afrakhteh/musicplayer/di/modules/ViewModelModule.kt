package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.di.ViewModelKey
import com.afrakhteh.musicplayer.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AllMusicViewModel::class)
    fun bindAllMusicViewModel(viewModel: AllMusicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentlyAddedViewModel::class)
    fun bindRecentlyViewModel(viewModel: RecentlyAddedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LikedViewModel::class)
    fun bindLikedViewModel(viewModel: LikedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayListViewModel::class)
    fun bindPlayListViewModel(viewModel: PlayListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayListMusicsViewModel::class)
    fun bindPlayListWithMusicsViewModel(viewModel: PlayListMusicsViewModel): ViewModel
}