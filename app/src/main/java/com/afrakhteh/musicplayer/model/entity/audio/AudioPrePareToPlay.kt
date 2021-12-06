package com.afrakhteh.musicplayer.model.entity.audio

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Audio_table")
data class AudioPrePareToPlay(
        @PrimaryKey
        val id: Int,
        val path: String,
        val musicName: String,
        val musicArtist: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            requireNotNull(parcel.readString()),
            requireNotNull(parcel.readString()),
            requireNotNull(parcel.readString()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(path)
        parcel.writeString(musicName)
        parcel.writeString(musicArtist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioPrePareToPlay> {
        override fun createFromParcel(parcel: Parcel): AudioPrePareToPlay {
            return AudioPrePareToPlay(parcel)
        }

        override fun newArray(size: Int): Array<AudioPrePareToPlay?> {
            return arrayOfNulls(size)
        }
    }
}
