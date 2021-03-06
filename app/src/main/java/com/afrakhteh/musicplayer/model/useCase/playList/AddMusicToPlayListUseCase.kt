package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import javax.inject.Inject

class AddMusicToPlayListUseCase @Inject constructor(private val repository: MusicRepository) {
    suspend operator fun invoke(item: AllMusicsEntity) {
        repository.addMusicToPlayList(item)
        repository.increasePlayListSize(item.playListId)
    }
}