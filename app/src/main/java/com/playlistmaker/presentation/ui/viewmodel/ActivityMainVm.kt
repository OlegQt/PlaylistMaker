package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.SingleLiveEvent
import com.playlistmaker.presentation.models.Screen


class ActivityMainVm():ViewModel() {
    var currentScreen:String = App.instance.currentScreen

    val screen = SingleLiveEvent<String>()

    init {
        // Если сохраненный экран отличается от текущего
        // производим запуск другого activity
        if (!currentScreen.equals(Screen.MAIN.screenName)){
            loadAnotherActivity(currentScreen)
        }
    }

    fun loadAnotherActivity(screenName: String){
        screen.value = screenName
    }
}