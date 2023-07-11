package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.playlistmaker.presentation.SingleLiveEvent
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.util.Creator


class ActivityMainVm(app:Application):AndroidViewModel(app) {
    private val loadLastScreenUseCase = Creator.getCreator().provideLoadLastScreenUseCase(app.baseContext)
    private var currentScreen:String = loadLastScreenUseCase.execute()

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