package com.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.playlistmaker.Logic.Player
import com.playlistmaker.R
import com.playlistmaker.Theme.App
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.ui.models.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding
import com.playlistmaker.domain.impl.MusicPlayerInteractorImpl
import com.playlistmaker.domain.impl.MusicTrackRepositoryImpl
import java.text.SimpleDateFormat
import java.util.*

class ActivityPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    //private val mediaPlayer: Player = Player()
    lateinit var musPlayer: MusicPlayerInteractorImpl

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var durationRunnable: Runnable

    private fun setUiBehaviour() {

        // Функция считывает текущую позицию прослушивания трека.
        // Закидывает себя в очередь на исполнение снова через 100мс
        this.durationRunnable = Runnable {
            changeDuration()
            // В случае, если проигрывание остановиться, функция не будет добавлена в очередь
            handler.postDelayed(durationRunnable, 100)
        }

        // Слушатель кнопки назад
        binding.playerBtnBack.setOnClickListener { finish() }

        // Слушатель кнопки Играть/Пауза
        binding.playerBtnPlay.setOnClickListener {
            if (musPlayer.getPlayerState()) musPlayer.pauseMusicTrack()
            else musPlayer.playMusicTrack()
        }




    }

    private fun showTrackInfo(track: MusicTrackDto): Boolean {
        val releaseDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val releaseYear = SimpleDateFormat().format(releaseDate)

        // Подгружаем текстовые данные по треку
        with(binding) {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            PlayerLblAlbum.text = track.collectionName.toString()
            PlayerLblGenre.text = track.primaryGenreName
            PlayerLblCountry.text = track.country
            PlayerLblFullDuration.text = track.getStringTime()
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

    private fun changeBtnPlayPause(state: ButtonState) {
        if (state == ButtonState.BUTTON_PLAY) binding.playerBtnPlay.setImageResource(R.drawable.play_track)
        else binding.playerBtnPlay.setImageResource(R.drawable.playerpause)
    }

    private fun trackPlayingTimeUpdate(start: Boolean) {
        if (start) {
            handler.post(durationRunnable)
        } else {
            handler.removeCallbacks(durationRunnable)
        }
    }

    private fun changeDuration() {
        binding.playerPlayTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(musPlayer.getCurrentPlayingPosition())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)


        val playerStateListener = Player.OnPlayerStateListener { playerState ->
            when (playerState) {
                Player.STATE_PREPARED -> changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                Player.STATE_PLAYING -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PAUSE)
                    trackPlayingTimeUpdate(true) // Запустили считывание перемени проигрывания
                }

                Player.STATE_COMPLETE -> changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                Player.STATE_PAUSED -> {
                    changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                    trackPlayingTimeUpdate(false) // Запустили считывание перемени проигрывания
                }
            }
        }
        val musTrackRepo = MusicTrackRepositoryImpl()
        musPlayer = MusicPlayerInteractorImpl(musTrackRepo, Player(playerStateListener))
        musPlayer.preparePlayer()

        setUiBehaviour() // Вешаем слушателей на элементы UI
        val currentTrack = musTrackRepo.getCurrentMusicTrack()
        if (currentTrack != null) this.showTrackInfo(currentTrack)

    }

    override fun onPause() {
        super.onPause()
        musPlayer.pauseMusicTrack()
        changeBtnPlayPause(ButtonState.BUTTON_PLAY)
        trackPlayingTimeUpdate(false)
    }

    override fun finish() {
        super.finish()
        App.instance.saveCurrentScreen(Screen.MAIN) // Сохраняем данные о переходе на главный экран приложения
    }

    override fun onDestroy() {
        super.onDestroy()
        // Удаляем наш runnable из очереди и выключаем плеер
        handler.removeCallbacks(durationRunnable)
        musPlayer.stopMusicTrack()
    }

    enum class ButtonState {
        BUTTON_PLAY,
        BUTTON_PAUSE
    }
}