package com.afrakhteh.musicplayer.model.use_case

import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import javax.inject.Inject

class AddToFaveUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(item: AudioPrePareToPlay): Long {
        return repository.addToFave(item)
    }
}