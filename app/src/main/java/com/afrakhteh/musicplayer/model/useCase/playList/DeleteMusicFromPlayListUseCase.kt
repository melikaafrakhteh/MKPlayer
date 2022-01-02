package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class DeleteMusicFromPlayListUseCase @Inject constructor(
        private val repository: MusicRepository,
        private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(path: String, music: AllMusicsEntity) {
        repository.removeMusicFromPlayList(path)
        playListRepository.decreasePlayListSize(music.playListId)
    }
}