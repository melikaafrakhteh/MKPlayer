package com.afrakhteh.musicplayer.model.repository.player

import io.reactivex.Observable

interface AudioDetailsRepository {
    suspend fun fetchAudioWaveData(path: String): Observable<ArrayList<Int>>
}