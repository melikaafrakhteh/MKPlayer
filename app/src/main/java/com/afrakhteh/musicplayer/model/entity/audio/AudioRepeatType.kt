package com.afrakhteh.musicplayer.model.entity.audio

sealed class AudioRepeatType {
    object RepeatOneMusic : AudioRepeatType()
    object RepeatAllMusicList : AudioRepeatType()
    object ShuffleMusicList : AudioRepeatType()
}
