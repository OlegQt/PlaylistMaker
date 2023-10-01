package com.playlistmaker.presentation.ui.activities

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playlistmaker.R
import com.playlistmaker.databinding.FragmentPlayerBinding
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.presentation.ui.viewmodel.PlayerVm
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityPlayer : AppCompatActivity() {
    private lateinit var binding: FragmentPlayerBinding
    private val vm: PlayerVm by viewModel()

    private fun setUiBehaviour() {
        binding.playerBtnBack.setOnClickListener { finish() }

        binding.playerBtnPlay.setOnClickListener { vm.pushPlayPauseButton() }

        binding.addToFavBtn.setOnClickListener { vm.pushAddToFavourite() }

        binding.temporalBtn.setOnClickListener { vm.showFavTracks() }
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

        // Определяем, есть ли трек в базе данных избранных треков и
        // выбираем соответствующую иконку
        if (track.isFavourite) {
            binding.addToFavBtn.setImageResource(R.drawable.red_heart)
        } else binding.addToFavBtn.setImageResource(R.drawable.like_track)

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
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.getCurrentMusTrack.observe(this) {
            this.showTrackInfo(it)
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
                    // Старт coroutine ответственной за обновление времени проигрывания трека
                    vm.trackDurationProvider()
                }

                else -> {
                    showAlertDialog("prepared")
                }
            }

        }

        vm.getPlayingTime.observe(this) { binding.playerPlayTime.text = it.toTimeMmSs() }

        vm.errorMsg.observe(this) { showAlertDialog(msg = it) }

        // Извлекаем музыкальный трек из intent и делаем текущим
        // Проверка на версию
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY, MusicTrack::class.java)
        } else {
            intent?.getParcelableExtra(MusicTrack.TRACK_KEY) as MusicTrack?
        }

        if (track != null) vm.loadCurrentMusicTrack(track)

        setUiBehaviour() // Вешаем слушателей на элементы UI


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