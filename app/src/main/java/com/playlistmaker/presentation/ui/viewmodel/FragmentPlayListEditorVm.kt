package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FragmentPlayListEditorVm(
    private val playListController: PlayListController
) : ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private var temporalPlayList: PlayList = PlayList()

    private val _screenState = MutableStateFlow<String>("Yexy")
    val screenState = _screenState as StateFlow<String>

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

        //val listOfTracks = Gson().fromJson(temporalPlayList.trackList, Array<Long>::class.java).toList()

        val listOfTracks = when (temporalPlayList.trackList) {
            "" -> emptyList()
            else -> Gson().fromJson(temporalPlayList.trackList, Array<Long>::class.java).toList()
        }

        viewModelScope.launch {
            val lst = playListController.getMusicTracksMatchedIds(listOfTracks.toList())
            _screenState.value = lst.map { "${it.trackName} \n" }.toString()
            //_errorMsg.postValue(lst.map { "${it.trackName} \n" }.toString())
        }
    }
}
