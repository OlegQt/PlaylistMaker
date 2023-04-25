package com.playlistmaker.itunes

import com.playlistmaker.Logic.Track
import java.text.SimpleDateFormat
import java.util.*

data class ItunesTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    fun toTrack(): Track {
        return Track(
            this.trackName,
            this.artistName,
            this.timeToString(trackTimeMillis), // Need fun
            this.artworkUrl100
        )
    }
    fun timeToString(time:Long)= SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}
