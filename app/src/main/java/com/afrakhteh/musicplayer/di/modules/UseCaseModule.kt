package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.model.useCase.IsMusicLikedUseCase
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
}