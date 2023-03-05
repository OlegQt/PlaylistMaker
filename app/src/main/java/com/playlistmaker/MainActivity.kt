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
        val displayIntent = Intent(this.baseContext, ActivitySettings::class.java)


        btnSearch.setOnClickListener { }
        btnMedia.setOnClickListener { }
        btnSettings?.setOnClickListener { startActivity(displayIntent) }

    }
}