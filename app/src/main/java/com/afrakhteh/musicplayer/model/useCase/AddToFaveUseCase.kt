package com.afrakhteh.musicplayer.model.useCase

import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import javax.inject.Inject

class AddToFaveUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(item: FavoriteEntity) {
        repository.addToFave(item)
    }
}