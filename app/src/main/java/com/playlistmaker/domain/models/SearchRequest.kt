package com.playlistmaker.domain.models

sealed interface SearchRequest{
    class MusicSearchRequest(val searchParam:String):SearchRequest
}