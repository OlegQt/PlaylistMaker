package com.playlistmaker.appstart

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        applyTheme(getCurrentTheme())
    }

    fun applyTheme(theme: Int) {
        saveAndChangeTheme(theme)
        when (theme) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun saveAndChangeTheme(theme: Int) {
        sharedPreferences.edit().putInt(THEME_MODE, theme).apply()
    }

    fun getCurrentTheme(): Int {
        return sharedPreferences.getInt(THEME_MODE, 0)
    }


    companion object {
        // Константы времени компиляции
        const val PREFERENCES = "APP_PREFERENCES"
        const val THEME_MODE = "key_for_dark_mode_switch"
        const val SEARCH_HISTORY = "key_for_search_history"
        const val CURRENT_PLAYING_TRACK = "key_for_saving_current_track"

        // Для удобного доступа к App
        lateinit var instance: App
            private set // Менять значение можно только внутри этого класса
    }
}