package com.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imgBack: ImageView = findViewById(R.id.backToMainActivity)
        val mainScreen = Intent(this.baseContext, MainActivity::class.java)

        imgBack.setOnClickListener{finish()}
    }
}