package com.afrakhteh.musicplayer.di.modules

import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.viewModel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
 class ViewModelFactoryModule {

   /* @Binds
    internal abstract fun  bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory*/

    @Provides
    fun provideViewModelFactory(factory:ViewModelProviderFactory ):ViewModelProvider.Factory {
        return factory
    }
}