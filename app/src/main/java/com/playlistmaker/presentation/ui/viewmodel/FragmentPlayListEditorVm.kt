package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import kotlinx.coroutines.launch

open class FragmentPlayListEditorVm(
    private val playListController: PlayListController
) : FragmentNewPlayListVm(playListController) {
    val playListOpened:PlayList? = null

    fun loadPlaylistById(id:Long){
        _errorMsg.value ="Load $id"
        viewModelScope.launch {
            playListController.loadPlayListById(id)
        }
    }

}