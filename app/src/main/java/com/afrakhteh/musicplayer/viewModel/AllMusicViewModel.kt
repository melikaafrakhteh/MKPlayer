package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.playList.AddMusicToPlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.AddNewPlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.GetAllPlayListUseCase
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.views.main.state.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllMusicViewModel @Inject constructor(
        var repository: MusicRepository,
        private var addNewPlayListUseCase: AddNewPlayListUseCase,
        private var getAllPlayListUseCase: GetAllPlayListUseCase,
        private var addMusicToPlayListUseCase: AddMusicToPlayListUseCase
) : ViewModel() {
    private val pState = MutableLiveData(MusicState())
    val state: LiveData<MusicState> get() = pState

    private val pPlayList = MutableLiveData<List<PlayListEntity>>()
    val playList: LiveData<List<PlayListEntity>> get() = pPlayList

    private var allMusicJob: Job? = null
    private var deleteMusicJob: Job? = null

    fun fetchAllMusic() {
        try {
            allMusicJob = CoroutineScope(Dispatchers.IO).launch {
                val resultList: List<MusicEntity> = repository.getAllMusic()
                pState.postValue(state.value?.copy(musicItems = resultList))
            }
        } catch (e: Exception) {
            pState.value = state.value?.copy(
                    message = SingleEvent("There was an error while receiving the musics")
            )
        }
    }

    fun deleteMusicFromList(musicEntity: MusicEntity) {
        deleteMusicJob = CoroutineScope(Dispatchers.IO).launch {
            repository.deleteItemFromList(musicEntity.path)
            val list = pState.value?.musicItems as MutableList
            list.remove(musicEntity)
            pState.postValue(pState.value?.copy(musicItems = list as List<MusicEntity>))
        }
    }

    suspend fun createANewPlayList(item: PlayListEntity) {
        addNewPlayListUseCase.invoke(item)
    }

    suspend fun fetchAllPlayList() {
        getAllPlayListUseCase.invoke().let {
            pPlayList.postValue(it)
        }
    }

    suspend fun addMusicToPlayList(item: MusicEntity, playListPosition: Int) {
        addMusicToPlayListUseCase.invoke(AllMusicsEntity(
                item.index,
                item.path,
                item.name ?: "",
                item.artist ?: "",
                playListPosition)
        )
    }

    override fun onCleared() {
        allMusicJob?.cancel()
        deleteMusicJob?.cancel()
        super.onCleared()
    }
}