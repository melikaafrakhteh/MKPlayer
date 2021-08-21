package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.viewModel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module


@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}