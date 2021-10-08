package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job

class PlayerViewModel(
        private val repository: AudioDetailsRepository
) : ViewModel() {

    private lateinit var job: Job
    private val disposable = CompositeDisposable()

    private val pWaveList = MutableLiveData<ArrayList<Int>>()
    val waveList: LiveData<ArrayList<Int>> get() = pWaveList

    fun getAllAudioWaveData(path: String) {
        /*     job = CoroutineScope(Dispatchers.IO).launch {

                 repository.fetchAudioWaveData(path).subscribeBy(
                     onError = {
                         Log.e("playerViewModel", "player view model error in subscription: $it")
                     },
                     onNext = {
                         pWaveList.value = it
                     }
                 ).addTo(disposable)
             }*/
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}