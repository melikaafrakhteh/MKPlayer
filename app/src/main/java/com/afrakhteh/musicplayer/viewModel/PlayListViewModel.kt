package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import com.afrakhteh.musicplayer.model.useCase.playList.DeleteOnePlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.GetAllPlayListUseCase
import com.afrakhteh.musicplayer.views.main.state.PlayListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayListViewModel @Inject constructor(
        private val getAllPlayListUseCase: GetAllPlayListUseCase,
        private val deleteOnePlayListUseCase: DeleteOnePlayListUseCase,
        val repository: PlayListRepository
) : ViewModel() {

    private var playListJob: Job? = null
    private var deletePlayListJob: Job? = null


    private val pState = MutableLiveData<PlayListState>()
    val state: LiveData<PlayListState> get() = pState

    fun fetchAllPlayList() {
        playListJob = CoroutineScope(Dispatchers.IO).launch {
            getAllPlayListUseCase.invoke().let {
                pState.postValue(PlayListState(it))
            }
        }
    }

    fun deleteSelectedPlayList(item: PlayListEntity) {
        deletePlayListJob = CoroutineScope(Dispatchers.Main).launch {
            deleteOnePlayListUseCase.invoke(item)
        }
    }

    override fun onCleared() {
        playListJob?.cancel()
        deletePlayListJob?.cancel()
        super.onCleared()
    }
}