package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.di.scopes.UseCaseScope
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

    @UseCaseScope
    @Provides
    fun provideAddToFaveUseCase(repository: FavoriteRepository): AddToFaveUseCase {
        return AddToFaveUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideDeleteFromFaveUseCase(repository: FavoriteRepository): DeleteFromFaveUseCase {
        return DeleteFromFaveUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideGetAllFaveListUseCase(
            favoriteRepository: FavoriteRepository,
            musicRepository: MusicRepository
    ): GetAllFaveListUseCase {
        return GetAllFaveListUseCase(favoriteRepository, musicRepository)
    }

    @UseCaseScope
    @Provides
    fun provideIsMusicLikeUseCase(repository: FavoriteRepository): IsMusicLikedUseCase {
        return IsMusicLikedUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideAddMusicToPlayListUseCase(repository: MusicRepository): AddMusicToPlayListUseCase {
        return AddMusicToPlayListUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideDeleteMusicFromPlayListUseCase(
            repository: MusicRepository
    ): DeleteMusicFromPlayListUseCase {
        return DeleteMusicFromPlayListUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideAddNewPlayList(repository: PlayListRepository,
                              musicRepository: MusicRepository): AddNewPlayListUseCase {
        return AddNewPlayListUseCase(repository, musicRepository)
    }

    @UseCaseScope
    @Provides
    fun provideDeleteOnePlayList(repository: PlayListRepository): DeleteOnePlayListUseCase {
        return DeleteOnePlayListUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideGetAllPlayListUseCase(repository: PlayListRepository): GetAllPlayListUseCase {
        return GetAllPlayListUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideGetAllPlayListWithMusicsUseCase(
            repository: PlayListRepository
    ): GetPlayListWithMusicsUseCase {
        return GetPlayListWithMusicsUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideGetAllPlayListTitleUseCase(repository: MusicRepository): GetAllPlayListTitlesUseCase {
        return GetAllPlayListTitlesUseCase(repository)
    }

    @UseCaseScope
    @Provides
    fun provideFindPlayListIdUseCase(repository: MusicRepository): FindMusicPlayListIdUseCase {
        return FindMusicPlayListIdUseCase(repository)
    }
}