package com.playlistmaker.domain.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val previewUrl: String,
    var isFavourite:Boolean
):Parcelable
{
    companion object{
        const val TRACK_KEY = "track_key"
    }
}



