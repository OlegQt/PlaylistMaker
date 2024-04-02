package com.playlistmaker.appstart

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.playlistmaker.di.dataModule
import com.playlistmaker.di.domainModule
import com.playlistmaker.di.presentationModule
import com.playlistmaker.domain.models.Theme
import com.playlistmaker.domain.usecase.apppreferences.SettingsController
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            AndroidLogger(Level.DEBUG)
            // Передаём все необходимые модули
            modules(dataModule, domainModule, presentationModule)
        }

        // Загружаем сохраненную тему
        val settingsController: SettingsController = getKoin().get()
        applyTheme(settingsController.loadMode())

        // Initialise Peko
        PermissionRequester.initialize(context = baseContext)
    }

    private fun applyTheme(themeMode: Theme) {
        when (themeMode) {
            Theme.DAY_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        // Для удобного доступа к App
        lateinit var instance: App private set

        const val MUSIC_PLAYER_SERVICE_TRACK_MODEL = "com.playlist_maker.track_url"
    }
}