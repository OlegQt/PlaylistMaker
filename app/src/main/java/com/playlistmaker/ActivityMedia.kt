package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen

class ActivityMedia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
    }

    override fun finish() {
        super.finish()
        Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }
}