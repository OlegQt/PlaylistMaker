package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityMainBinding
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.fragments.SearchFragment

class MainActivity : AppCompatActivity(), AlertMessaging {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
        binding.rootNavigationBar.setupWithNavController(navController = navHostFragment.navController)

        //val navControl = navHostFragment.navController
        //navControl.navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
    }

    override fun showAlertDialog(alertMessage: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(alertMessage)
            .setTitle("Dialog")
            .setNeutralButton("OK", null)
            .show()
    }
}