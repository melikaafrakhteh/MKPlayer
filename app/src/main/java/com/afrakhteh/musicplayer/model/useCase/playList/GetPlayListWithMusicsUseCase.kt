package com.afrakhteh.musicplayer.model.useCase.playList

import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.entity.audio.MusicsOfAPlayList
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import javax.inject.Inject

class GetPlayListWithMusicsUseCase @Inject constructor(private val repository: PlayListRepository) {
    suspend operator fun invoke(id: Int): List<MusicsOfAPlayList> {
        return repository.getPlayListWithMusics(id).map {
            MusicsOfAPlayList(
                    playList = AllPlayListEntity(it.playList.playListId, it.playList.title),
                    musicList = it.musicList.map { allMusic ->
                        MusicEntity(
                                name = allMusic.name,
                                path = allMusic.path,
                                artist = allMusic.artist,
                                index = allMusic.musicId
                        )
                    }
            )
        }
    }
}