package com.playlistmaker.Theme

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.playlistmaker.itunes.ItunesTrack
import java.util.prefs.Preferences

class App : Application() {
    var darkTheme = true;
    var currentMusicTrack:ItunesTrack? = null
    var currentScreen:String = String()

    lateinit var sharedPreferences: SharedPreferences

    companion object {
        // Константы времени компиляции
        const val PREFERENCES = "APP_PREFERENCES"
        const val DARK_MODE_KEY = "key_for_dark_mode_switch"
        const val SEARCH_HISTORY = "key_for_search_history"
        const val CURRENT_SCREEN = "key_for_saving_current_string"
        const val CURRENT_MUSIC = "key_for_saving_current_track"
        const val TAG_LOG = "DEBUG"

        // Для удобного доступа к App
        lateinit var instance: App
            private set // Менять значение можно только внутри этого класса

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Прогружаем данные по стилю
        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        // Грузим значение переключателя темы, если он есть в сохраненке
        this.darkTheme = sharedPreferences.getBoolean(DARK_MODE_KEY, false)
        // Принудительно меняем тему всего приложения
        this.switchTheme(darkTheme)

        // Грузим текущий экран
        currentScreen = sharedPreferences.getString(CURRENT_SCREEN,Screen.MAIN.screenName).toString()
    }

    fun switchTheme(darkThemeMode: Boolean) {
        darkTheme = darkThemeMode
        when (darkThemeMode) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        sharedPreferences.edit().putBoolean(DARK_MODE_KEY, darkThemeMode).apply()
    }

    fun saveCurrentScreen(screen:Screen){
        sharedPreferences.edit().putString(CURRENT_SCREEN,screen.screenName).apply()
    }
}