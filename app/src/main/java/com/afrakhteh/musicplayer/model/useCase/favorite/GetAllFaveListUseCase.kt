package com.afrakhteh.musicplayer.model.useCase.favorite

import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.favorite.FavoriteRepository
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import javax.inject.Inject

class GetAllFaveListUseCase @Inject constructor(
        private val faveRepository: FavoriteRepository,
        private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): List<MusicEntity> {
        val likedIDList = faveRepository.getAllFaveList()
        return musicRepository.getMusicListById(likedIDList.map { it.id })
    }
}