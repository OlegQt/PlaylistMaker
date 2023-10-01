package com.playlistmaker.presentation.models

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList

sealed interface FragmentPlaylistsState{
    data class NothingFound(val msg: Any?) : FragmentPlaylistsState
    data class Content(val playLists:List<PlayList>) :  FragmentPlaylistsState
}