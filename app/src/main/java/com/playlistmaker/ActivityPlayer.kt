package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding

class ActivityPlayer : AppCompatActivity() {
    lateinit var binding:ActivityPlayerBinding


    private fun setBehaviour(){
        binding.playerBtnBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)
        setBehaviour()
    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
    }
}