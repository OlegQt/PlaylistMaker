package com.playlistmaker.domain.usecase.apppreferences

import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.repository.SettingsRepository

class SettingsControllerImpl(private val settingsRepository: SettingsRepository) :
    SettingsController {
    override fun loadMode(): Theme {
        return settingsRepository.loadThemeMode()
    }

    override fun safeMode(themeMode: Theme) {
        settingsRepository.safeThemeMode(themeMode)
    }
}