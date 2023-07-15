package com.playlistmaker.presentation.models

import com.playlistmaker.domain.models.MusicTrack

sealed interface ActivitySearchState {
    data class NothingFound(val msg:Any?) : ActivitySearchState
    data class InternetTroubles(val msg:Any?):ActivitySearchState
    data class MusicSearchContent(val music: ArrayList<MusicTrack>) : ActivitySearchState
    data class Loading(val msg:Any?):ActivitySearchState
    data class HistoryMusicContent(val music: ArrayList<MusicTrack>) : ActivitySearchState
    data class InitialState(val msg:Any?):ActivitySearchState
}