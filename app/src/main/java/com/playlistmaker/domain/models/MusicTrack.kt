package com.playlistmaker.domain.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicTrack(
    val trackName: String="",
    val artistName: String="",
    val trackTimeMillis: Long=0,
    val artworkUrl100: String="",
    val trackId: Long=0,
    val collectionName: String="",
    val releaseDate: String="",
    val primaryGenreName: String="",
    val country: String="",
    val previewUrl: String="",
    var isFavourite:Boolean=false
):Parcelable
{
    companion object{
        const val TRACK_KEY = "track_key"
    }
}



