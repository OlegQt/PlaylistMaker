package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityPlayer : AppCompatActivity() {
    lateinit var btnBack: Button

    fun deployUi() {
        btnBack = findViewById(R.id.player_btn_back)

    }

    fun setBehaviour(){
        btnBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        deployUi()
        setBehaviour()
    }
}