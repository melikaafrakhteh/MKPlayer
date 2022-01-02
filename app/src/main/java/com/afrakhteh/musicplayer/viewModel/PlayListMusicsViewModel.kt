package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.playList.DeleteMusicFromPlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.GetPlayListWithMusicsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayListMusicsViewModel @Inject constructor(
        private val getPlayListWithMusicsUseCase: GetPlayListWithMusicsUseCase,
        private val deleteMusicFromPlayListUseCase: DeleteMusicFromPlayListUseCase,
        val repository: MusicRepository
) : ViewModel() {

    private var getMusicsOfPlayListJob: Job? = null
    private var removeMusicOfPlayListJob: Job? = null

    private val pAllPlayListMusics = MutableLiveData<List<MusicEntity>>()
    val allPlayListMusic: LiveData<List<MusicEntity>> get() = pAllPlayListMusics

    fun fetchMusicOfThisPlayList(position: Int) {
        getMusicsOfPlayListJob = CoroutineScope(Dispatchers.IO).launch {
            getPlayListWithMusicsUseCase.invoke(position).let {
                pAllPlayListMusics.postValue(
                        it.firstOrNull()?.musicList
                )
            }
        }
    }

    fun deleteOneMusicFromPlayList(item: MusicEntity, position: Int) {
        removeMusicOfPlayListJob = CoroutineScope(Dispatchers.IO).launch {
            deleteMusicFromPlayListUseCase.invoke(
                    item.path,
                    AllMusicsEntity(
                            musicId = item.index,
                            path = item.path,
                            name = item.name ?: "",
                            artist = item.artist ?: "",
                            playListId = position
                    )
            )
        }
    }

    override fun onCleared() {
        getMusicsOfPlayListJob?.cancel()
        removeMusicOfPlayListJob?.cancel()
        super.onCleared()
    }
}