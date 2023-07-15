package com.playlistmaker.appstart

import android.app.Application
import android.content.SharedPreferences
import com.playlistmaker.util.Creator
import androidx.appcompat.app.AppCompatDelegate
import com.playlistmaker.di.dataModule
import com.playlistmaker.di.domainModule
import com.playlistmaker.di.presentationModule
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.usecase.SettingsController
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private lateinit var settingsController:SettingsController

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            // Передаём все необходимые модули
            modules(dataModule, domainModule, presentationModule)
        }

        // Загружаем сохраненную тему
        settingsController = Creator.getCreator().provideSettingsController(applicationContext)
        applyTheme(settingsController.loadMode())
    }

    private fun applyTheme(themeMode:Theme) {
        when (themeMode) {
            Theme.DAY_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        // Для удобного доступа к App
        lateinit var instance: App
            private set // Менять значение можно только внутри этого класса
    }
}