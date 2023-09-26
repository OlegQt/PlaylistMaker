package com.playlistmaker.presentation.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class PlayListVm : ViewModel() {

    private val _btnCreateEnable = MutableLiveData<Boolean>()
    val btnCreateEnable = _btnCreateEnable as LiveData<Boolean>

    var playListName:String? = null
    var playListDescription:String? = null
    var playListCover: File? = null
    var playListCoverUri: Uri? = null

    init {
        _btnCreateEnable.value = false
    }

    fun nothingChanged():Boolean{
        return playListCoverUri==null &&
            playListDescription.isNullOrEmpty() &&
            playListName.isNullOrEmpty()

    }

    fun changePlayListName(newName: String) {
        // Если текстовое поле названия плейлиста пустое, скрываем кнопку создать
        _btnCreateEnable.value = newName.isNotEmpty()
        playListName = newName
    }
}