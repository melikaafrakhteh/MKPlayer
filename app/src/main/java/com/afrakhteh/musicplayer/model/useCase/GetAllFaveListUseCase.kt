package com.afrakhteh.musicplayer.model.useCase

import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFaveListUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(): Flow<List<AudioPrePareToPlay>> {
        return repository.getAllFaveList()
    }
}