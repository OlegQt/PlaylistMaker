package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.playlistmaker.appstart.App
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.util.Creator

class ActivitySettingsVm(app:Application) : AndroidViewModel(app) {
    private val settingsController = Creator.getCreator().provideSettingsController(app.baseContext)
    val isNightMode = settingsController.loadMode().themeCode==1


    fun switchTheme() {
        when (settingsController.loadMode()) {
            Theme.DAY_THEME -> applyTheme(Theme.NIGHT_THEME)
            Theme.NIGHT_THEME -> applyTheme(Theme.DAY_THEME)
        }
    }

    private fun applyTheme(themeMode: Theme) {
        when (themeMode) {
            Theme.DAY_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        settingsController.safeMode(themeMode)
    }

}