package com.playlistmaker.data.dto

data class CompoundSearchResponse(
    val resultCount: Int,
    val results: ArrayList<MusicTrackDto>
):SearchResponse()
