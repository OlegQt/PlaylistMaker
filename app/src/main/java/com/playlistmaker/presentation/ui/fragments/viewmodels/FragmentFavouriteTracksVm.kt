package com.playlistmaker.presentation.ui.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playlistmaker.presentation.models.FragmentFavouriteTracksState

class FragmentFavouriteTracksVm : ViewModel() {
    private val fragmentState = MutableLiveData<FragmentFavouriteTracksState>()
    fun getFragmentState(): LiveData<FragmentFavouriteTracksState> = this.fragmentState

    init {
        loadFavouriteTracks()
    }

    private fun loadFavouriteTracks() {
        // TODO: Здесь прописать загрузку избранных треков
        // Временно ставим принудительную заглушку
        fragmentState.value = FragmentFavouriteTracksState.NothingFound(null)
    }
}
