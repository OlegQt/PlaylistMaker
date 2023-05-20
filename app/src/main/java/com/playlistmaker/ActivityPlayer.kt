package com.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding

class ActivityPlayer : AppCompatActivity() {
    lateinit var binding: ActivityPlayerBinding


    private fun setBehaviour() {
        binding.playerBtnBack.setOnClickListener { finish() }
    }

    private fun showTrackInfo() {
        val track = App.instance.currentMusicTrack
        if (track!=null){
            binding.playerTrackName.text = track.trackName
            binding.playerArtistName.text = track.artistName

            binding.PlayerLblAlbum.text = if(track.collectionName.isNullOrEmpty()) "" else track.collectionName
            binding.PlayerLblGenre.text = track.primaryGenreName
            binding.PlayerLblCountry.text = track.country
            binding.PlayerLblFullDuration.text=track.getStringTime()
            binding.PlayerLblYear.text=track.releaseDate.substring(0,4)

            val art = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
            val px = (this.baseContext.resources.displayMetrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT)
            val radius = 8*px
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
    }
}