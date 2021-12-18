package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.db.relations.PlayListWithMusics
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class GetPlayListWithMusicsUseCase @Inject constructor(private val repository: PlayListRepository) {
    suspend operator fun invoke(id: Int): List<PlayListWithMusics> {
        return repository.getPlayListWithMusics(id)
    }
}