package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class AddNewPlayListUseCase @Inject constructor(
        private val repository: PlayListRepository,
        private val musicRepository: MusicRepository) {
    suspend operator fun invoke(item: PlayListEntity, music: AllMusicsEntity) {
        repository.addANewPlayList(item)
        musicRepository.addMusicToPlayList(music)
        musicRepository.increasePlayListSize(music.playListId)
    }
}