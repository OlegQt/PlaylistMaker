package com.playlistmaker.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityMainBinding
import com.playlistmaker.presentation.ui.fragments.MediaLibraryFragment
import com.playlistmaker.presentation.ui.fragments.SearchFragment
import com.playlistmaker.presentation.ui.fragments.SettingsFragment
import com.playlistmaker.presentation.ui.viewmodel.ActivityMainVm
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val btnMedia = findViewById<Button>(R.id.media)
        //val btnSearch = findViewById<Button>(R.id.search)
        //val btnSettings: Button = findViewById(R.id.settings)

        val vm: ActivityMainVm by viewModel()

        binding.rootNavigationBar.setOnItemSelectedListener{
            var temporalFragment:Fragment? = null
            when(it.itemId){
                R.id.pageA -> temporalFragment=SearchFragment()
                R.id.PageB ->temporalFragment=MediaLibraryFragment()
                R.id.PageC -> temporalFragment = SettingsFragment()
            }
            this.manualFragmentOpening(temporalFragment)
        }

        if(savedInstanceState==null){
            supportFragmentManager.commit {
                add(R.id.root_placeholder, MediaLibraryFragment())
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

    private fun manualFragmentOpening(internalFragment: Fragment?):Boolean{
        return if (internalFragment!=null) {
            supportFragmentManager.commit {
                replace(R.id.root_placeholder, internalFragment)
                addToBackStack(null)
            }
            true
        } else false
    }
}