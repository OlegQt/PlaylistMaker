package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityMainBinding
import com.playlistmaker.presentation.models.AlertMessaging

class MainActivity : AppCompatActivity(), AlertMessaging {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_placeholder) as NavHostFragment
        binding.rootNavigationBar.setupWithNavController(navController = navHostFragment.navController)

        navController = navHostFragment.navController


        // Настройка скрытия навигационной панели при отображении определенных фрагментов
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment -> binding.rootNavigationBar.visibility = View.GONE
                else -> binding.rootNavigationBar.visibility = View.VISIBLE
            }

        }
    }

    fun navigateBack() {
        navController.navigateUp()
    }

    override fun showAlertDialog(alertMessage: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(alertMessage)
            .setTitle("Dialog")
            .setNeutralButton("OK", null)
            .show()
    }

    override fun showSnackBar(messageToShow: String) {
        Snackbar.make(binding.rootNavigationBar, messageToShow, Snackbar.LENGTH_LONG).show()
    }
}