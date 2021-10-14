package com.afrakhteh.musicplayer.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScope
class PlayerViewModel @Inject constructor(
        private val repository: AudioDetailsRepository
) : ViewModel() {

    private lateinit var job: Job
    private val disposable = CompositeDisposable()

    private val pWaveList = MutableLiveData<ArrayList<Int>>()
    val waveList: LiveData<ArrayList<Int>> get() = pWaveList

    fun getAllAudioWaveData(path: String) {
        job = CoroutineScope(Dispatchers.Main).launch {
            repository.fetchAudioWaveData(path)
                    .subscribeBy(
                            onError = {
                                Log.e("playerViewModel", "player view model error in subscription: $it")
                            },
                            onNext = {
                                pWaveList.postValue(it)
                                disposable.clear()
                                Log.d("player view model", "$it")
                            }
                    ).addTo(disposable)
        }
    }

    fun mappedData(gains: ArrayList<Int>): List<Int> {
        val minValue = requireNotNull(gains.minOrNull())
        val maxValue = requireNotNull(gains.maxOrNull())
        val diff = maxValue - minValue
        return gains.map { items ->
            (((items - minValue) * 100f) / diff).toInt()
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}