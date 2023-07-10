package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.presentation.ui.activities.ActivityMedia
import com.playlistmaker.presentation.ui.activities.ActivityPlayer
import com.playlistmaker.presentation.ui.activities.ActivitySearch
import com.playlistmaker.presentation.ui.activities.ActivitySettings
import com.playlistmaker.presentation.ui.activities.MainActivity
import com.playlistmaker.util.Creator


class ActivityMainVm(application: Application):AndroidViewModel(application) {
    val app = application
    var currentScreen:String = App.instance.currentScreen

    private var errorMessage = MutableLiveData<String>()
    fun getErrorMsg():LiveData<String> = errorMessage

    init {
        // Если сохраненный экран отличается от текущего
        // производим запуск другого activity
        if (!currentScreen.equals(Screen.MAIN.screenName)){
            loadAnotherActivity(currentScreen)
        }


        val cA = Creator.getCreator()
        val cB = Creator.getCreator()
        val cC = Creator.getCreator()
        cC.count=54
        errorMessage.value = "$cA"

    }

    fun loadAnotherActivity(screenName: String){
        val intent = when (screenName) {
            Screen.SEARCH.screenName -> Intent(this.app,ActivitySearch::class.java)
            Screen.SETTINGS.screenName -> Intent(this.app,ActivitySettings::class.java)
            Screen.PLAYER.screenName -> Intent(this.app,ActivityPlayer::class.java)
            Screen.MEDIA.screenName -> Intent(this.app,ActivityMedia::class.java)
            else -> Intent(this.app,MainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            app.startActivity(intent)
        } catch (error: Exception) {
            Toast.makeText(app.baseContext, error.message, Toast.LENGTH_SHORT).show()
        }
    }
}