package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playlistmaker.presentation.models.FragmentPlaylistsState

class FragmentPlayListsVm:ViewModel() {

    // LiveData для состояния экрана фрагмента
    // FragmentPlaylistsState.NothingFound - Показывать заглушку
    // FragmentPlaylistsState.Content - Показывать плейлисты
    private val playlistState = MutableLiveData<FragmentPlaylistsState>()
    fun getFragmentState(): LiveData<FragmentPlaylistsState> = this.playlistState

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        // TODO: Здесь прописать загрузку избранных треков
        // Временно ставим принудительную заглушку
        playlistState.value = FragmentPlaylistsState.NothingFound(null)

        // Ниже проверка, если загружен плейлист, то заглушка не показывается
        //playlistState.value = FragmentPlaylistsState.Content(mapOf())
    }
}