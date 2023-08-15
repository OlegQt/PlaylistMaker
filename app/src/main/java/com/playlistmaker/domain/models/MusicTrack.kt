package com.playlistmaker.domain.models

import android.os.Parcel
import android.os.Parcelable

data class MusicTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString()?: "",
        parcel.readLong(),
        parcel.readString()?: "",
        parcel.readLong(),
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeLong(trackId)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicTrack> {
        const val TRACK_KEY = "track_key"
        override fun createFromParcel(parcel: Parcel): MusicTrack {
            return MusicTrack(parcel)
        }

        override fun newArray(size: Int): Array<MusicTrack?> {
            return arrayOfNulls(size)
        }
    }

}
