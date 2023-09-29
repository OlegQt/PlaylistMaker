package com.playlistmaker.presentation.ui.activities

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.R
import com.playlistmaker.databinding.ActivityPlayerBBinding
import com.playlistmaker.databinding.ActivityPlayerBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.presentation.models.AlertMessaging
import com.playlistmaker.presentation.ui.fragments.MusicPlayerFragment
import com.playlistmaker.presentation.ui.fragments.SettingsFragment
import com.playlistmaker.presentation.ui.fragments.medialibrary.PlayListsFragment
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityPlayerB : AppCompatActivity(),AlertMessaging {
    private lateinit var binding: ActivityPlayerBBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.standardBottomSheet.visibility = View.GONE

        if (savedInstanceState == null) {
            val loadedMusicTrack = extractMusicTrack()

            // Отображаем родительский фрагмент
            supportFragmentManager.commit {
                add(binding.fragmentHolder.id,MusicPlayerFragment.newInstance(loadedMusicTrack))
            }
        }
    }

    private fun extractMusicTrack():MusicTrack?{
        // Извлекаем музыкальный трек из intent и делаем текущим
        // Проверка на версию
        val track:MusicTrack? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY, MusicTrack::class.java)
        } else {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY) as MusicTrack?
        }
        return track
    }

    override fun showAlertDialog(alertMessage: String) {
        //
    }

    override fun showSnackBar(messageToShow: String) {
        Snackbar.make(binding.fragmentHolder,messageToShow,Snackbar.LENGTH_INDEFINITE)
            .setAction("OK"){}
            .setTextMaxLines(20)
            .show()
    }
}