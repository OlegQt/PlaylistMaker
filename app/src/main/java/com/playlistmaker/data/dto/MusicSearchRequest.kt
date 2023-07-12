package com.playlistmaker.data.dto

import com.playlistmaker.domain.models.SearchRequest

data class MusicSearchRequest(val songName:String):SearchRequest(songName) {
}