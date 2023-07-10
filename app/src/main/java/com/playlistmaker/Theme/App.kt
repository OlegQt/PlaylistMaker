package com.playlistmaker.Theme

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.Screen

class App : Application() {
    var currentMusicTrack: MusicTrack? = null
    var currentScreen: String = String()
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate() {
        super.onCreate()

        instance = this

        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        // Грузим текущий экран
        currentScreen =
            sharedPreferences.getString(CURRENT_SCREEN, Screen.MAIN.screenName).toString()
        // Грузим играющий трек
        currentMusicTrack = loadCurrentPlayingTrack()

        applyTheme(getCurrentTheme())
    }

    fun applyTheme(theme: Int) {
        saveAndChangeTheme(theme)
        when (theme) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun saveAndChangeTheme(theme: Int) {
        sharedPreferences.edit().putInt(THEME_MODE, theme).apply()
    }

    fun getCurrentTheme(): Int {
        return sharedPreferences.getInt(THEME_MODE, 0)
    }

    fun saveCurrentScreen(screen: Screen) {
        sharedPreferences.edit().putString(CURRENT_SCREEN, screen.screenName).apply()
        currentScreen = screen.screenName
    }

    private fun loadCurrentPlayingTrack(): MusicTrack? {
        val jsonTrack = sharedPreferences.getString(CURRENT_PLAYING_TRACK, "")
        return if (!jsonTrack.isNullOrEmpty()) {
            Gson().fromJson(jsonTrack, MusicTrack::class.java)
        } else null
    }

    companion object {
        // Константы времени компиляции
        const val PREFERENCES = "APP_PREFERENCES"
        const val THEME_MODE = "key_for_dark_mode_switch"
        const val SEARCH_HISTORY = "key_for_search_history"
        const val CURRENT_SCREEN = "key_for_saving_current_string"
        const val CURRENT_PLAYING_TRACK = "key_for_saving_current_track"
        const val TAG_LOG = "DEBUG"

        // Для удобного доступа к App
        lateinit var instance: App
            private set // Менять значение можно только внутри этого класса

    }
}