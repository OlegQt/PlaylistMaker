package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.usecase.dbfavourite.LoadFavouriteTracksIdsUseCase
import com.playlistmaker.domain.usecase.dbfavourite.LoadFavouriteTracksUseCase
import com.playlistmaker.presentation.models.FragmentFavouriteTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentFavouriteTracksVm(
    private val loadFavouriteTracks: LoadFavouriteTracksUseCase
) : ViewModel() {
    private val fragmentState = MutableLiveData<FragmentFavouriteTracksState>()
    fun getFragmentState(): LiveData<FragmentFavouriteTracksState> = this.fragmentState

    init {
        loadFavouriteTracks()
    }

    fun loadFavouriteTracks() {
        // Временно ставим принудительную заглушку
        fragmentState.value = FragmentFavouriteTracksState.NothingFound(null)

        // TODO: Здесь прописать загрузку избранных треков
        viewModelScope.launch(Dispatchers.IO) {
            loadFavouriteTracks.execute().collect() {favTracksList ->
                withContext(Dispatchers.Main){
                    // Check if favourite tracks list is not empty below
                    if (favTracksList.isNotEmpty()) fragmentState.postValue(FragmentFavouriteTracksState.Content(favTracksList))
                    else fragmentState.value = FragmentFavouriteTracksState.NothingFound(null)
                }
            }
        }
    }
}
