package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.playlistmaker.appstart.App
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.usecase.SettingsController
import com.playlistmaker.util.Creator
import org.koin.core.Koin
import org.koin.java.KoinJavaComponent.getKoin

class ActivitySettingsVm() : ViewModel() {
    //private val settingsController = Creator.getCreator().provideSettingsController(app.baseContext)
    private val settingsController:SettingsController = getKoin().get()

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