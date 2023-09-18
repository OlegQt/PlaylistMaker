package com.playlistmaker.presentation.models

import com.playlistmaker.domain.models.MusicTrack

sealed interface FragmentFavouriteTracksState {
    data class NothingFound(val msg: Any?) : FragmentFavouriteTracksState
    data class Content(val tracksList: List<MusicTrack>) :
        FragmentFavouriteTracksState
}