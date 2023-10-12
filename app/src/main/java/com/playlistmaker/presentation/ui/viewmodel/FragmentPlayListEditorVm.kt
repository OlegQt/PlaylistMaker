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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FragmentPlayListEditorVm(
    private val playListController: PlayListController
) : ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private var currentPlayListOnScreen: PlayList = PlayList()

    private val _screenState = MutableStateFlow<PlayListEditorFragment.ScreenState>(
        PlayListEditorFragment.ScreenState.NoData(null)
    )
    val screenState = _screenState as StateFlow<PlayListEditorFragment.ScreenState>

    private var startPlayerApp = SingleLiveEvent<MusicTrack>()
    val getStartPlayerCommand = startPlayerApp as LiveData<MusicTrack>

    fun evaluatePlayList(idPlayList: Long) {
        viewModelScope.launch {
            playListController.loadPlayListById(id = idPlayList).collect {
                currentPlayListOnScreen = it
                extractTracksFromPlayList(it)
            }
        }
    }

    private fun extractTracksFromPlayList(playList: PlayList) {
        val listOfTracks = when (playList.trackList) {
            "" -> emptyList()
            else -> Gson().fromJson(playList.trackList, Array<Long>::class.java).toList()
        }

        viewModelScope.launch {
            playListController.getFlowMusicTracksMatchedIds(listOfTracks).collect {
                _screenState.value = PlayListEditorFragment.ScreenState.Content(
                    playList = playList,
                    it
                )
            }
        }

    }

    private fun deleteUnusedMusicTrack(trackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playListController.checkIfTrackIsUnused(trackId)
        }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        viewModelScope.launch {
            val longTask = async(Dispatchers.IO) {
                playListController.deleteTrackFromPlayList(currentPlayListOnScreen, trackId)

            }
            longTask.invokeOnCompletion {
                if (it == null) deleteUnusedMusicTrack(trackId)
                else _errorMsg.value = it.message
            }

        }
    }
}
