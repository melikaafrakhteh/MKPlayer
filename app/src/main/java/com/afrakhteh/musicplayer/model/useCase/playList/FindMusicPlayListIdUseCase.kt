package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import javax.inject.Inject

class FindMusicPlayListIdUseCase @Inject constructor(private val repository: MusicRepository) {
    suspend operator fun invoke(): Int {
        return if (repository.getAllPlayListTitle().isEmpty()) {
            repository.findFirstPlayListId() + 1
        } else
            repository.findPlayListId() + 1
    }
}