package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen

class MainActivity : AppCompatActivity() {
    var btnSettings: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMedia = findViewById<Button>(R.id.media)
        val btnSearch = findViewById<Button>(R.id.search)
        // Попробовал объявить переменную кнопки в поле класса без later-int
        btnSettings = findViewById(R.id.settings)

        // Объявление интентов
        val settingsScreen = Intent(this, ActivitySettings::class.java)
        val mediaScreen = Intent(this, ActivityMedia::class.java)
        val searchScreen = Intent(this, ActivitySearch::class.java)
        val playerScreen = Intent(this, ActivityPlayer::class.java)

        btnSearch.setOnClickListener { startActivity(searchScreen) }
        btnMedia.setOnClickListener { startActivity(mediaScreen) }
        btnSettings?.setOnClickListener { startActivity(settingsScreen) }

        // Ниже проверяем на каком экране было закрыто приложение в прошлом запуске
        // Восстанавливаем нужный экран
        when (App.instance.currentScreen) {
            Screen.SEARCH.screenName -> startActivity(searchScreen)
            Screen.PLAYER.screenName -> startActivity(playerScreen)
            Screen.SETTINGS.screenName -> startActivity(settingsScreen)
        }

    }
}