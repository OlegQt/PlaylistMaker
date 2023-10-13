package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController

class FragmentChangePlaylistVm(playListController: PlayListController) :FragmentNewPlayListVm(
    playListController
) {

    init {
        _errorMsg.value ="Change Playlist"
    }
}