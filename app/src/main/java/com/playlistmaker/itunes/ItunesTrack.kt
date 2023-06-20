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
    val country:String,
    val previewUrl:String

) {

    private fun timeToString(time:Long): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    fun getStringTime():String = this.timeToString(this.trackTimeMillis)
    override fun toString(): String {
        return with(StringBuilder()){
            append("$artistName  \n")
            append("$trackName  \n")
            append("$collectionName  \n")
            append("$country  \n")

        }.toString()
    }
}
