package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.di.ViewModelKey
import com.afrakhteh.musicplayer.viewModel.AllMusicViewModel
import com.afrakhteh.musicplayer.viewModel.PlayerViewModel
import com.afrakhteh.musicplayer.viewModel.RecentlyAddedViewModel
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
}