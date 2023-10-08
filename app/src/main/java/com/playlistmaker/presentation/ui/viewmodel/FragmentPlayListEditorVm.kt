package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import com.playlistmaker.presentation.SingleLiveEvent
import com.playlistmaker.presentation.ui.fragments.PlayListEditorFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FragmentPlayListEditorVm(
    private val playListController: PlayListController
) : ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private var temporalPlayList: PlayList = PlayList()

    private val _screenState = MutableStateFlow<PlayListEditorFragment.ScreenState>(
        PlayListEditorFragment.ScreenState.NoData(null)
    )
    val screenState = _screenState as StateFlow<PlayListEditorFragment.ScreenState>

    private var startPlayerApp = SingleLiveEvent<MusicTrack>()
    val getStartPlayerCommand = startPlayerApp as LiveData<MusicTrack>

    fun setError(str: String) {
        _errorMsg.value = str
    }

    fun evaluatePlayList(idPlayList: Long) {
        viewModelScope.launch {
            temporalPlayList = playListController.loadPlayListById(id = idPlayList)
            getMusicFromPlayList(temporalPlayList)
        }
    }

    private fun getMusicFromPlayList(playList: PlayList) {
        val listOfTracks = when (temporalPlayList.trackList) {
            "" -> emptyList()
            else -> Gson().fromJson(temporalPlayList.trackList, Array<Long>::class.java).toList()
        }

        viewModelScope.launch {
            val lst = playListController.getMusicTracksMatchedIds(listOfTracks.toList())
            _screenState.value = PlayListEditorFragment.ScreenState.Content(
                playList = temporalPlayList,
                lst
            )
            //_errorMsg.postValue(lst.map { "${it.trackName} \n" }.toString())
        }
    }
}
