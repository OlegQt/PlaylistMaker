package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.playlistmaker.presentation.SingleLiveEvent


class ActivityMainVm():ViewModel() {
    private val screen = SingleLiveEvent<String>()

    fun loadAnotherActivity(screenName: String){
        screen.value = screenName
    }
}