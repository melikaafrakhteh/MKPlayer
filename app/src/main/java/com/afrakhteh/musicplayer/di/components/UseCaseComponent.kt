package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.UseCaseModule
import com.afrakhteh.musicplayer.di.scopes.UseCaseScope
import com.afrakhteh.musicplayer.model.useCase.favorite.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.IsMusicLikedUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.*
import dagger.Component

@UseCaseScope
@Component(
        modules = [UseCaseModule::class],
        dependencies = [RepositoryComponent::class]
)
interface UseCaseComponent {
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
}