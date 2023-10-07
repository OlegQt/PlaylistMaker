package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentPlayListEditorVm(
    private val playListController: PlayListController
):ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    fun setError(str:String){
        _errorMsg.value = str
    }

    fun evaluatePlayList(idPlayList:Long){
        viewModelScope.launch {
            val lists = playListController.loadAllPlayLists().collect{
                _errorMsg.value = it[idPlayList.toInt()].name
            }
        }
    }
}