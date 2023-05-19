package com.playlistmaker.itunes

import com.playlistmaker.Logic.Track
import java.text.SimpleDateFormat
import java.util.*

data class ItunesTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId:Long,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country:String
) {
    fun toTrack(): Track {
        return Track(
            this.trackName,
            this.artistName,
            this.timeToString(trackTimeMillis), // Need fun
            this.artworkUrl100,
            this.trackId,
            this.collectionName,
            this.releaseDate,
            this.primaryGenreName,
            this.country
        )
    }
    fun timeToString(time:Long)= SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}
