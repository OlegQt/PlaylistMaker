package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.Theme

interface SettingsRepository {
    fun loadThemeMode():Theme
    fun safeThemeMode(theme: Theme)
}