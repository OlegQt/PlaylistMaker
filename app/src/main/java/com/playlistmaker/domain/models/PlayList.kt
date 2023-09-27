package com.playlistmaker.domain.models

data class PlayList(
    val id: Long,
    val name: String,
    val description: String,
    val cover: String,
    val trackList: String,
    val quantity: Int
)
