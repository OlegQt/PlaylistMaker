package com.playlistmaker.itunes

import com.playlistmaker.Logic.Track

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
            this.trackTimeMillis.toString(), // Need fun
            this.artworkUrl100
        )
    }
}
