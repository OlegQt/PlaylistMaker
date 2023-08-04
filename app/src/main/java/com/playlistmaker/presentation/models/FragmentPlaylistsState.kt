package com.playlistmaker.presentation.models

import com.playlistmaker.domain.models.MusicTrack

sealed interface FragmentPlaylistsState{
    data class NothingFound(val msg: Any?) : FragmentPlaylistsState
    data class Content(val playLists: Map<String,ArrayList<MusicTrack>>) :
        FragmentPlaylistsState
}