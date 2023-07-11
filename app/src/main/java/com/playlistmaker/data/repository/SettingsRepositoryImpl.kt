package com.playlistmaker.data.repository

import android.content.Context
import com.playlistmaker.appstart.App
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.repository.SettingsRepository

private const val PREFERENCES = "APP_PREFERENCES"
private const val THEME_MODE = "key_for_dark_mode_switch"

class SettingsRepositoryImpl(context: Context):SettingsRepository {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    override fun loadThemeMode(): Theme {
        val themeCode = sharedPreferences.getInt(THEME_MODE, 0)

        // ThemeCode:Int на случай расширения количества стилей (тем)
        return when(themeCode){
            0 -> Theme.DAY_THEME
            1 -> Theme.NIGHT_THEME
            else -> Theme.DAY_THEME
        }
    }

    override fun safeThemeMode(theme: Theme) {
        sharedPreferences.edit().putInt(THEME_MODE, theme.themeCode).apply()
    }
}