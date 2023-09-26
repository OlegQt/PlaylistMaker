package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class PlayListVm : ViewModel() {

    private val _btnCreateEnable = MutableLiveData<Boolean>()
    val btnCreateEnable = _btnCreateEnable as LiveData<Boolean>

    private var playListName:String? = null
    private var playListDescription:String? = null
    private var playListCover: File? = null

    init {
        _btnCreateEnable.value = false
    }

    fun nothingChanged():Boolean{
        return playListCover==null &&
            playListDescription.isNullOrEmpty() &&
            playListName.isNullOrEmpty()

    }

    fun changePlayListName(newName: String) {
        // Если текстовое поле названия плейлиста пустое, скрываем кнопку создать
        _btnCreateEnable.value = newName.isNotEmpty()
        playListName = newName
    }
}