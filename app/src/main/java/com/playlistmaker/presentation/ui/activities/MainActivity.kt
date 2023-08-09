package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.playlistmaker.R
import com.playlistmaker.presentation.ui.fragments.SettingsFragment
import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val btnMedia = findViewById<Button>(R.id.media)
        //val btnSearch = findViewById<Button>(R.id.search)
        //val btnSettings: Button = findViewById(R.id.settings)

        val vm: ActivityMainVm by viewModel()

        if(savedInstanceState==null){
            supportFragmentManager.commit {
                add(R.id.root_placeholder, SettingsFragment())
            }
        }

/*        vm.screen.observe(this) {
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
                *//*Snackbar.make(
                    btnMedia,
                    getString(R.string.error_loading_activity),
                    Snackbar.LENGTH_SHORT
                ).show()*//*
            }
        }*/

        //btnSearch.setOnClickListener { vm.loadAnotherActivity(Screen.SEARCH.screenName) }
        //btnMedia.setOnClickListener { vm.loadAnotherActivity(Screen.MEDIA.screenName) }
        //btnSettings.setOnClickListener { vm.loadAnotherActivity(Screen.SETTINGS.screenName) }
    }
}