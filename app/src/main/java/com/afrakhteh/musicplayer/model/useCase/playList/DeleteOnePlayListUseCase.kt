package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class DeleteOnePlayListUseCase @Inject constructor(private val repository: PlayListRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteOnePlayList(id)
    }
}