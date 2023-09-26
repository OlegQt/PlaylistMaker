package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayListVm : ViewModel() {

    private val _btnCreateEnable = MutableLiveData<Boolean>()
    val btnCreateEnable = _btnCreateEnable as LiveData<Boolean>

    init {
        _btnCreateEnable.value = false
    }

    fun changePlayListName(newName: String) {
        // Если текстовое поле названия плейлиста пустое, скрываем кнопку создать
        _btnCreateEnable.value = newName.isNotEmpty()
    }
}