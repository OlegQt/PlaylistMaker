package com.playlistmaker

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
import com.playlistmaker.Theme.App
import com.playlistmaker.Theme.Screen
import com.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class ActivityPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val mediaPlayer: Player = Player()
    private val handler = Handler(Looper.getMainLooper())
    private var time = 0
    lateinit var timerRunnable: Runnable

    private fun setBehaviour() {
        // Слушатель кнопки назад
        binding.playerBtnBack.setOnClickListener { finish() }

        // Слушатель кнопки Игарть/Пауза
        binding.playerBtnPlay.setOnClickListener {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pausePlayer()
                changeBtnPlayPause(ButtonState.BUTTON_PLAY)
            } else {
                mediaPlayer.startPlayer()
                changeBtnPlayPause(ButtonState.BUTTON_PAUSE)
            }
        }

        // Слушатель для изменения состояния медиаплеера
        mediaPlayer.setOnPlayerStateListener { playerState ->
            when (playerState) {
                Player.STATE_PREPARED -> {
                    mediaPlayer.startPlayer() // Стартуем проигрывание трека по готовности
                    changeBtnPlayPause(ButtonState.BUTTON_PAUSE)
                }
                Player.STATE_COMPLETE -> changeBtnPlayPause(ButtonState.BUTTON_PLAY)
                else -> Toast.makeText(baseContext, "${Player.PlayerState}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun showTrackInfo(): Boolean {
        val track = App.instance.currentMusicTrack ?: return false

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

        // Запускаем медиаПлеер
        mediaPlayer.preparePlayer(track.previewUrl)

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
        if (state==ButtonState.BUTTON_PLAY) binding.playerBtnPlay.setImageResource(R.drawable.play_track)
        else binding.playerBtnPlay.setImageResource(R.drawable.playerpause)
    }

    private fun startTimer(){
        val run = object :Runnable {
            override fun run() {
                //changeDuration()
                handler.postDelayed(this,100)
            }
        }
        handler.post(run)

    }

    private fun changeDuration(){

        Toast.makeText(binding.root.context,"1",Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.root,(++time).toString(),Snackbar.LENGTH_INDEFINITE).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Сохраняем текущий экран как главный в sharedPrefs
        App.instance.saveCurrentScreen(Screen.PLAYER)
        setBehaviour() // Вешаем слушателей на элементы UI
        showTrackInfo() // Аналог функции bind для заполнения полей для текущего музыкального трека

        startTimer()

    }

    override fun finish() {
        super.finish()
        App.instance.saveCurrentScreen(Screen.MAIN) // Сохраняем данные о переходе на главный экран приложения
        mediaPlayer.turnOffPlayer() // Завершаем плеер
    }

    enum class ButtonState {
        BUTTON_PLAY,
        BUTTON_PAUSE
    }
}