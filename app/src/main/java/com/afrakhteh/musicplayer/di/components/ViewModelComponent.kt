package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.MainViewModelModule
import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.views.fragments.AllMusicFragment
import dagger.Component

@ViewModelScope
@Component(
        modules = [ViewModelFactoryModule::class,
                   MainViewModelModule::class],
        dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {

    fun inject(allMusicFragment: AllMusicFragment)
}