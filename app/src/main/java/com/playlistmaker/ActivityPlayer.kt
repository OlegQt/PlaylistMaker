package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen

class ActivityPlayer : AppCompatActivity() {
    lateinit var btnBack: ImageView

    fun deployUi() {
        btnBack = findViewById(R.id.player_btn_back)
    }

    fun setBehaviour(){
        btnBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)

        deployUi()
        setBehaviour()
    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }
}