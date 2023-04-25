package com.playlistmaker.Theme

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.prefs.Preferences

class App : Application() {
    var darkTheme = true;
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        // Константы времени компиляции
        const val PREFERENCES = "APP_PREFERENCES"
        const val DARK_MODE_KEY = "key_for_dark_mode_switch"

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
    }

    fun switchTheme(darkThemeMode: Boolean) {
        darkTheme = darkThemeMode
        when (darkThemeMode) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        sharedPreferences.edit().putBoolean(DARK_MODE_KEY, darkThemeMode).apply()
    }
}