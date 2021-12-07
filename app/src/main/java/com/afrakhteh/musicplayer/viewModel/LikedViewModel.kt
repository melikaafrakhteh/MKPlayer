package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.views.main.state.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class LikedViewModel @Inject constructor(
        private val getAllFaveListUseCase: GetAllFaveListUseCase,
        val repository: MusicRepository
) : ViewModel() {

    private val pState = MutableLiveData<MusicState>()
    val state: LiveData<MusicState> get() = pState

    private var job: Job? = null

    fun getAllMusicLikedList() {
        job = CoroutineScope(Dispatchers.IO).launch {
            getAllFaveListUseCase.invoke().let {
                pState.postValue(MusicState(it))
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}
