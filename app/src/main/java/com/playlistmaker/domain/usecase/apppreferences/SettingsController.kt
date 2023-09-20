package com.playlistmaker.domain.usecase.apppreferences

import android.util.Log
import com.playlistmaker.domain.models.Theme

interface SettingsController {
    fun loadMode():Theme
    fun safeMode(themeMode:Theme)
}