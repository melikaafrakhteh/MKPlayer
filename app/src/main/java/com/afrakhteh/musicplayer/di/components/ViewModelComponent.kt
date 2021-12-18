package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.UseCaseModule
import com.afrakhteh.musicplayer.di.modules.ViewModelFactoryModule
import com.afrakhteh.musicplayer.di.modules.ViewModelModule
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.useCase.favorite.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.IsMusicLikedUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.*
import com.afrakhteh.musicplayer.views.main.fragments.*
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
    fun exposeIsMusicLiked(): IsMusicLikedUseCase
    fun exposeAddMusicToPlayList(): AddMusicToPlayListUseCase
    fun exposeAddNewPlayList(): AddNewPlayListUseCase
    fun exposeDeleteMusicFromPlayList(): DeleteMusicFromPlayListUseCase
    fun exposeDeleteOnePlayList(): DeleteOnePlayListUseCase
    fun exposeGetAllPlayList(): GetAllPlayListUseCase
    fun exposeGetPlayListWithMusics(): GetPlayListWithMusicsUseCase

    fun inject(allMusicFragment: AllMusicFragment)
    fun inject(playerActivity: PlayerActivity)
    fun inject(recentlyFragment: RecentlyFragment)
    fun inject(likedFragment: LikedFragment)
    fun inject(playListFragment: PlayListFragment)
    fun inject(playListMusicsFragment: PlayListMusicsFragment)
}