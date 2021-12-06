package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.use_case.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.use_case.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.use_case.GetAllFaveListUseCase
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