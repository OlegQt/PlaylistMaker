package com.playlistmaker.data.dto

data class MusicSearchResponse(
    val resultCount: Int,
    val results: ArrayList<MusicTrackDto>
):MusicResponse()
