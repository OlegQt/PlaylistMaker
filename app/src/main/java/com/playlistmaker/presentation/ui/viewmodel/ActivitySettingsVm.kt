package com.playlistmaker.presentation.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.usecase.SettingsController
import org.koin.java.KoinJavaComponent.getKoin

class ActivitySettingsVm(
    private val settingsController: SettingsController
) : ViewModel() {
    val isNightMode = settingsController.loadMode().themeCode == 1

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