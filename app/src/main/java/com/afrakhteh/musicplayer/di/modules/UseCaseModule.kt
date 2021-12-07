package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.useCase.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.GetAllFaveListUseCase
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
    fun provideGetAllFaveListUseCase(repository: FavoriteRepository): GetAllFaveListUseCase {
        return GetAllFaveListUseCase(repository)
    }
}