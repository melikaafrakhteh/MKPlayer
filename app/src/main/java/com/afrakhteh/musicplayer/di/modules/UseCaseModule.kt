package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import com.afrakhteh.musicplayer.model.useCase.favorite.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.IsMusicLikedUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideAddToFaveUseCase(repository: FavoriteRepository): AddToFaveUseCase {
        return AddToFaveUseCase(repository)
    }

    @Provides
    fun provideDeleteFromFaveUseCase(repository: FavoriteRepository): DeleteFromFaveUseCase {
        return DeleteFromFaveUseCase(repository)
    }

    @Provides
    fun provideGetAllFaveListUseCase(
            favoriteRepository: FavoriteRepository,
            musicRepository: MusicRepository
    ): GetAllFaveListUseCase {
        return GetAllFaveListUseCase(favoriteRepository, musicRepository)
    }

    @Provides
    fun provideIsMusicLikeUseCase(repository: FavoriteRepository): IsMusicLikedUseCase {
        return IsMusicLikedUseCase(repository)
    }

    @Provides
    fun provideAddMusicToPlayListUseCase(repository: MusicRepository): AddMusicToPlayListUseCase {
        return AddMusicToPlayListUseCase(repository)
    }

    @Provides
    fun provideDeleteMusicFromPlayListUseCase(
            repository: MusicRepository
    ): DeleteMusicFromPlayListUseCase {
        return DeleteMusicFromPlayListUseCase(repository)
    }

    @Provides
    fun provideAddNewPlayList(repository: PlayListRepository): AddNewPlayListUseCase {
        return AddNewPlayListUseCase(repository)
    }

    @Provides
    fun provideDeleteOnePlayList(repository: PlayListRepository): DeleteOnePlayListUseCase {
        return DeleteOnePlayListUseCase(repository)
    }

    @Provides
    fun provideGetAllPlayListUseCase(repository: PlayListRepository): GetAllPlayListUseCase {
        return GetAllPlayListUseCase(repository)
    }

    @Provides
    fun provideGetAllPlayListWithMusicsUseCase(
            repository: PlayListRepository
    ): GetPlayListWithMusicsUseCase {
        return GetPlayListWithMusicsUseCase(repository)
    }


}