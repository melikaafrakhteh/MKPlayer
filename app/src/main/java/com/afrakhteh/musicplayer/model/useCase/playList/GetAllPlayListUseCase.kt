package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class GetAllPlayListUseCase @Inject constructor(private val repository: PlayListRepository) {
    suspend operator fun invoke(): List<AllPlayListEntity> {
       val result = repository.getAllPlayLists()
        return result.map { playList ->
            AllPlayListEntity(
                    id = playList.playListId,
                    title = playList.title,
                    size = playList.size
            )
        }
    }
}