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
import com.playlistmaker.presentation.ui.fragments.PlayListViewerFragment
import com.playlistmaker.presentation.ui.recycleradapter.Syntactic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentPlayListViewerVm(
    private val playListController: PlayListController
) : ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private var currentPlayListOnScreen: PlayList = PlayList()
    fun getCurrentPlyList() = this.currentPlayListOnScreen

    private val _screenState = MutableStateFlow<PlayListViewerFragment.ScreenState>(
        PlayListViewerFragment.ScreenState.NoData(null)
    )
    val screenState = _screenState as StateFlow<PlayListViewerFragment.ScreenState>

    private var startPlayerApp = SingleLiveEvent<MusicTrack>()
    val getStartPlayerCommand = startPlayerApp as LiveData<MusicTrack>

    val exitTrigger = SingleLiveEvent<Boolean>()

    private var job: Job? = null


    fun evaluatePlayList(idPlayList: Long) {
        job = viewModelScope.launch {
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
                _screenState.value = PlayListViewerFragment.ScreenState.Content(
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

    fun deletePlayList() {
        viewModelScope.launch {
            job?.cancel()


            playListController.deletePlayList(currentPlayListOnScreen)
            exitTrigger.value = true

        }
    }

    fun deleteMultipleTrack(tracksIds: List<Long>) {
        tracksIds.forEach {
            deleteTrackFromPlaylist(it)
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

    fun generateMessage(playlist: PlayList, tracks: List<MusicTrack>): String {
        val sb = StringBuilder()
        sb.append(playlist.name).append("\n")
        if (playlist.description.isNotEmpty()) {
            sb.append(playlist.description).append("\n")
        }
        sb.append(Syntactic.getTrackEnding(tracks.size)).append("\n")
        tracks.forEachIndexed { index, track ->
            val trackDuration = getFullDurationFromLong((track.trackTimeMillis))
            sb.append("${index + 1}. ${track.artistName} - ${track.trackName} ($trackDuration)")
                .append("\n")
        }
        return sb.toString()
    }

    private fun getFullDurationFromLong(duration: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
    }

}
