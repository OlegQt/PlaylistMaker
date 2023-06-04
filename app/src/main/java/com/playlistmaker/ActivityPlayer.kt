package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.Logic.Player
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat

class ActivityPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val mediaPlayer:Player = Player()


    private fun setBehaviour() {
        binding.playerBtnBack.setOnClickListener { finish() }

        mediaPlayer.setOnPlayerStateListener { playerState ->
            when (playerState) {
                Player.STATE_PLAYING -> {
                    Toast.makeText(baseContext, "STATE_PLAYING", Toast.LENGTH_SHORT).show()
                }
                Player.STATE_PREPARED -> {
                    //Toast.makeText(baseContext, "STATE_PREPARED", Toast.LENGTH_SHORT).show()
                    mediaPlayer.startPlayer()
                }
                else -> Toast.makeText(baseContext, "", Toast.LENGTH_SHORT).show()
            }
        }

        //mediaPlayer.preparePlayer("в")

    }

    private fun showTrackInfo() {
        val track = App.instance.currentMusicTrack
        if (track != null) {
            binding.playerTrackName.text = track.trackName
            binding.playerArtistName.text = track.artistName
            binding.PlayerLblAlbum.text = track.collectionName.toString()
            binding.PlayerLblGenre.text = track.primaryGenreName
            binding.PlayerLblCountry.text = track.country
            binding.PlayerLblFullDuration.text = track.getStringTime()


            Snackbar.make(binding.root,"${track.previewUrl}",Snackbar.LENGTH_INDEFINITE).setTextMaxLines(20)
                .setAction("Ok") { }
                .show()

            mediaPlayer.preparePlayer(track.previewUrl)

            val format = SimpleDateFormat("yyyy")
            val time = format.parse(track.releaseDate)
            val year = format.format(time)

            binding.PlayerLblYear.text = year

            val art = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            val px = (this.baseContext.resources.displayMetrics.densityDpi
                    / DisplayMetrics.DENSITY_DEFAULT)
            val radius = 8 * px
            Glide
                .with(binding.root.context)
                .load(art)
                .placeholder(R.drawable.no_track_found)
                .centerCrop()
                .transform(RoundedCorners(radius)) //Params: roundingRadius – the corner radius (in device-specific pixels).
                .into(binding.playerArtWork)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)
        setBehaviour()
        showTrackInfo()

    }

    override fun finish() {
        super.finish()
        // Сохраняем данные о переходе на главный экран приложения
        App.instance.saveCurrentScreen(Screen.MAIN)
        // Завершаем плеер
        mediaPlayer.turnOffPlayer()
    }
}