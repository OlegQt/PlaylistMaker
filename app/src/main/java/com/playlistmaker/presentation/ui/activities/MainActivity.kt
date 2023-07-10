package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMedia = findViewById<Button>(R.id.media)
        val btnSearch = findViewById<Button>(R.id.search)
        val btnSettings: Button = findViewById(R.id.settings)

        val vm = ViewModelProvider(this)[ActivityMainVm::class.java]

        vm.screen.observe(this){
            val intent = when (it) {
                Screen.SEARCH.screenName -> Intent(this, ActivitySearch::class.java)
                Screen.SETTINGS.screenName -> Intent(this, ActivitySettings::class.java)
                Screen.PLAYER.screenName -> Intent(this, ActivityPlayer::class.java)
                Screen.MEDIA.screenName -> Intent(this, ActivityMedia::class.java)
                else -> Intent(this, MainActivity::class.java)
            }

            try {
                startActivity(intent)
            } catch (error: Exception) {

            }
        }



        btnSearch.setOnClickListener { vm.loadAnotherActivity(Screen.SEARCH.screenName) }
        btnMedia.setOnClickListener { vm.loadAnotherActivity(Screen.MEDIA.screenName) }
        btnSettings.setOnClickListener { vm.loadAnotherActivity(Screen.SETTINGS.screenName) }
    }
}