package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import javax.inject.Inject

class DeleteMusicFromPlayListUseCase @Inject constructor(
        private val repository: MusicRepository
) {
    suspend operator fun invoke(path: String) {
        repository.removeMusicFromPlayList(path)
    }
}