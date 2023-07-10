package com.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.presentation.models.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding
import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import java.text.SimpleDateFormat
import java.util.*

class ActivityPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var vm: PlayerVm

    private fun setUiBehaviour() {
        binding.playerBtnBack.setOnClickListener { finish() }

        binding.playerBtnPlay.setOnClickListener {
            vm.pushPlayPauseButton()
        }
    }

    private fun showTrackInfo(track: MusicTrack): Boolean {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val date = dateFormat.parse(track.releaseDate)
        val milliseconds = date?.time
        val releaseYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(milliseconds)


        // Подгружаем текстовые данные по треку
        with(binding) {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            PlayerLblAlbum.text = track.collectionName.toString()
            PlayerLblGenre.text = track.primaryGenreName
            PlayerLblCountry.text = track.country
            PlayerLblFullDuration.text = track.trackTimeMillis.toTimeMmSs()
            PlayerLblYear.text = releaseYear
        }

        // Изменили url картинки
        val artWorkHQ = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        // Получили плотность пикселя
        val px = (this.baseContext.resources.displayMetrics.densityDpi
                / DisplayMetrics.DENSITY_DEFAULT)

        // Рассчитываем радиус скругления краев imageView
        val radius = 8 * px

        // Загружаем картинку альбома в ImageView
        Glide
            .with(binding.root.context)
            .load(artWorkHQ)
            .placeholder(R.drawable.no_track_found)
            .centerCrop()
            .transform(RoundedCorners(radius)) //Params: roundingRadius – the corner radius (in device-specific pixels).
            .into(binding.playerArtWork)

        return true
    }

    // Функция расширения Long класса
    private fun Long.toTimeMmSs(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
    }

    private fun changeBtnPlayPause(state: ButtonState) {
        if (state == ButtonState.BUTTON_PLAY) binding.playerBtnPlay.setImageResource(R.drawable.play_track)
        else binding.playerBtnPlay.setImageResource(R.drawable.playerpause)
    }

    private fun showAlertDialog(msg: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(msg)
            .setTitle("Dialog")
            .setNeutralButton("OK", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)

        val factory = PlayerVm.getFactory(this.application)
        vm = ViewModelProvider(this, factory = factory)[PlayerVm::class.java]

        vm.getCurrentMusTrack.observe(this) {
            this.showTrackInfo(it)
            vm.preparePlayer(it.previewUrl)
        }

        vm.getPlayerState.observe(this) { state ->
            when (state) {
                PlayerState.STATE_PREPARED -> {}
                PlayerState.STATE_PAUSED -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                }

                PlayerState.STATE_COMPLETE -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                }

                PlayerState.STATE_PLAYING -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PAUSE)
                }

                else -> {
                    showAlertDialog("prepared")
                }
            }

        }

        vm.getPlayingTime.observe(this) { binding.playerPlayTime.text = it.toTimeMmSs() }

        vm.loadCurrentMusicTrack()

        setUiBehaviour() // Вешаем слушателей на элементы UI

    }

    override fun finish() {
        super.finish()
        startActivity( Intent(this, ActivitySearch::class.java))
    }

    override fun onPause() {
        super.onPause()
        vm.playPauseMusic(play = false)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.turnOffPlayer()
    }

    enum class ButtonState {
        BUTTON_PLAY,
        BUTTON_PAUSE
    }
}