package com.afrakhteh.musicplayer.model.repository.player

import com.afrakhteh.musicplayer.model.entity.wave.AudioWaveDataResult

interface AudioDetailsRepository {
    suspend fun fetchAudioWaveData(path: String): AudioWaveDataResult
}