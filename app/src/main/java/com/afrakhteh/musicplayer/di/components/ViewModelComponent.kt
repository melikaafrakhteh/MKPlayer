package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.UseCaseModule
import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.modules.ViewModelModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.useCase.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.views.main.fragments.AllMusicFragment
import com.afrakhteh.musicplayer.views.main.fragments.LikedFragment
import com.afrakhteh.musicplayer.views.main.fragments.RecentlyFragment
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import dagger.Component

@ViewModelScope
@Component(
        modules = [ViewModelFactoryModule::class,
            ViewModelModule::class,
            UseCaseModule::class],
        dependencies = [RepositoryComponent::class]
)
interface ViewModelComponent {

    fun exposeAddToFave(): AddToFaveUseCase
    fun exposeDeleteFromFave(): DeleteFromFaveUseCase
    fun exposeGetAllFaveList(): GetAllFaveListUseCase

    fun inject(allMusicFragment: AllMusicFragment)
    fun inject(playerActivity: PlayerActivity)
    fun inject(recentlyFragment: RecentlyFragment)
    fun inject(likedFragment: LikedFragment)
}