package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.di.ViewModelKey
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainActivityViewModel): ViewModel
}