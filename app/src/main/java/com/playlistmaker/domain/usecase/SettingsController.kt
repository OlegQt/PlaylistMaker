package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.repository.SettingsRepository

class SettingsController(private val settingsRepository:SettingsRepository) {
    fun loadMode():Theme{
        return settingsRepository.loadThemeMode()
    }

    fun safeMode(themeMode:Theme){
        settingsRepository.safeThemeMode(themeMode)
    }
}