package com.playlistmaker.appstart

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.playlistmaker.R
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

        // Register notification channel with the system
        registerNotificationChannel()
    }

    private fun applyTheme(themeMode: Theme) {
        when (themeMode) {
            Theme.DAY_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /**
     *  Caution: Check build version inside registerNotificationChannel function
     *  only Android 8.0 (API level 26) and higher,
     *  because the notification channels APIs aren't available in the Support Library.
     */
    // Вопрос выноса создания канала в Application обсуждался с наставником. Quote:
    // Лучше всего делать это в Application::onCreate, так как это ускорит запуск сервиса, что иногда критично.
    // Создание канала нотификаций все равно должно произойти, так что лучше вынести инициализации именно на момент старта приложения
    private fun registerNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val mChannel = NotificationChannel(
            MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID,
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.channel_description)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        // Для удобного доступа к App
        lateinit var instance: App private set

        const val MUSIC_PLAYER_SERVICE_TRACK_MODEL = "com.playlist_maker.track_url"
        const val MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID =
            "com.playlist.maker_notification_channel_id"
        const val MUSIC_PLAYER_PROGRESS_FREQUENCY = 200L
    }
}