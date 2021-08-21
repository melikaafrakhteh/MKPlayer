package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.di.ViewModelKey
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainViewModel(viewModel: MainActivityViewModel): ViewModel
}