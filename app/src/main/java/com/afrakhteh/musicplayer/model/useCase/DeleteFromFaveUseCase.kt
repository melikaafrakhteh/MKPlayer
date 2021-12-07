package com.afrakhteh.musicplayer.model.useCase

import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import javax.inject.Inject

class DeleteFromFaveUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(path: String) {
        repository.deleteFromFave(path)
    }
}