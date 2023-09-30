package com.playlistmaker.presentation.ui.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.databinding.ActivityPlayerBBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.fragments.MusicPlayerFragment
import com.playlistmaker.presentation.ui.fragments.NewPlaylistFragment

class ActivityPlayerB : AppCompatActivity(), AlertMessaging {
    private lateinit var binding: ActivityPlayerBBinding
    private var loadedMusicTrack: MusicTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.standardBottomSheet.visibility = View.GONE

        if (savedInstanceState == null) {
            loadedMusicTrack = extractMusicTrack()

            // Отображаем родительский фрагмент
            supportFragmentManager.commit {
                add(binding.fragmentHolder.id, MusicPlayerFragment.newInstance(loadedMusicTrack))
            }

            supportFragmentManager.addOnBackStackChangedListener {
                if (supportFragmentManager.backStackEntryCount == 0) {
                    showSnackBar("A fragment")
                }
                if (supportFragmentManager.backStackEntryCount == 1) {
                    showSnackBar("B fragment")
                }
            }
        }
    }

    private fun extractMusicTrack(): MusicTrack? {
        // Извлекаем музыкальный трек из intent и делаем текущим
        // Проверка на версию
        val track: MusicTrack? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY, MusicTrack::class.java)
        } else {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY) as MusicTrack?
        }
        return track
    }

    fun navigateToNewPlaylist() {
        supportFragmentManager.commit {
            replace(binding.fragmentHolder.id, NewPlaylistFragment())
            addToBackStack(null)
        }
    }

    fun navigateBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("LOG","Destroy ActivityPlayer")

    }

    override fun showAlertDialog(alertMessage: String) {
        //
    }

    override fun showSnackBar(messageToShow: String) {
        Snackbar.make(binding.fragmentHolder, messageToShow, Snackbar.LENGTH_INDEFINITE)
            .setAction("OK") {}
            .setTextMaxLines(20)
            .show()
    }
}