package com.afrakhteh.musicplayer.model.entity.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity

data class PlayListWithMusics(
        @Embedded val playList: PlayListEntity,
        @Relation(
                parentColumn = "playListId",
                entityColumn = "playListId"
        )
        val musicList: List<AllMusicsEntity>
)
