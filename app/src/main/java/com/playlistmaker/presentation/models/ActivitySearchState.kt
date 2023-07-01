package com.playlistmaker.presentation.models

import com.playlistmaker.domain.models.MusicTrack

sealed interface ActivitySearchState {
    data class NothingFound(val errorMsg: String) : ActivitySearchState
    data class InternetTroubles(val msg:Any?):ActivitySearchState
    data class Content(val music: ArrayList<MusicTrack>) : ActivitySearchState
    data class InitialState(val msg:Any?):ActivitySearchState
}