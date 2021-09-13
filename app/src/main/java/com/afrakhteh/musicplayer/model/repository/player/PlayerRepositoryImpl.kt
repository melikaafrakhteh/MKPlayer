package com.afrakhteh.musicplayer.model.repository.player

import com.afrakhteh.musicplayer.model.dataSource.AudioWaveDataSource

class PlayerRepositoryImpl(val data: AudioWaveDataSource) : PlayerRepository {

    override fun getAllMusicWaves(): List<Int> {

        val outPutWavesList: ArrayList<Int> = ArrayList()
        return outPutWavesList

    }
}