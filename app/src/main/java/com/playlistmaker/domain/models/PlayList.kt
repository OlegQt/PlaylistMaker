package com.playlistmaker.domain.models

data class PlayList(
    val id: Long=0,
    var name: String="",
    var description: String="",
    var cover: String="",
    val trackList: String="",
    val quantity: Int=0
)
