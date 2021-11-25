package com.afrakhteh.musicplayer.model.entity.wave

import io.reactivex.Observable

data class AudioWaveDataResult(
        val waveDataObservable: Observable<ArrayList<Int>>,
        val frameCountObservable: Observable<Int>
)
