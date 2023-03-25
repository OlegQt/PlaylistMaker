package com.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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

        //btnSearch.setOnClickListener { finish() }
        // Не понял ваш комментарий по поводу исопльзования finish()
        // он завершает тогда текущую активити и все. Объясните плз кодом, что вы имеете в виду?

        btnSearch.setOnClickListener { startActivity(searchScreen) }
        btnMedia.setOnClickListener { startActivity(mediaScreen) }
        btnSettings?.setOnClickListener { startActivity(settingsScreen) }

    }
}