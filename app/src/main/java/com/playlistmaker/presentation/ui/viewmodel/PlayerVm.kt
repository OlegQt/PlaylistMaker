package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.util.Creator


class PlayerVm(private val application: Application) : AndroidViewModel(application) {

    private val musTrackRepo = Creator.getCreator().getMusicTrackRepository(externalContext = application.baseContext)
    private val handler = android.os.Handler(Looper.getMainLooper())

    private var playingTime = MutableLiveData<Long>()
    private var playerState =
        MutableLiveData<PlayerState>() // Состояние плеера
    private val currentPlayingMusTrack = MutableLiveData<MusicTrack>() // Текущий загруженный трек

    // Геттеры для lifeData
    val getPlayerState = playerState as LiveData<PlayerState>
    val getCurrentMusTrack = this.currentPlayingMusTrack as LiveData<MusicTrack>
    val getPlayingTime = this.playingTime as LiveData<Long>

    // Переменная для хранения нашего плеера
    // В параметры передается объект реализующий функциональный интерфейс по обновлению состояния плеера
    private val musicalPlayer = Creator.getCreator().provideMusicPlayer(){playerState.postValue(it)}

    // Запускается при переходе на экран плеера
    // допускается перемещение в блок init
    fun loadCurrentMusicTrack() {
        val musTrack = musTrackRepo.getCurrentMusicTrack()
        if (musTrack != null) this.currentPlayingMusTrack.postValue(musTrack)


    }

    fun updatePlayingTime() {
        playingTime.postValue(musicalPlayer.getCurrentPos().toLong())
        handler.postDelayed({ updatePlayingTime() }, 100)

    }

    fun startTimer() {
        handler.post { updatePlayingTime() }
    }

    // Запускается в observer на getCurrentMusTrack
    // Функция будет запущена только при наличии трека в переменной getCurrentMusTrack
    fun preparePlayer(musTrackUrl: String) {
        musicalPlayer.preparePlayer(musTrackUrl)
    }

    fun pushPlayPauseButton() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> playPauseMusic(false)
            else -> playPauseMusic(true)
        }
    }

    fun playPauseMusic(play:Boolean){
        when(play){
            true -> {
                // Запускаем воспроизведение музыки
                // и запускаем таймер времени проигрывания музыки
                musicalPlayer.playMusic()
                startTimer()
            }
            false -> {
                // Останавливаем воспроизведение музыки
                // и удаляем таймер времени проигрывания музыки
                musicalPlayer.pauseMusic()
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    fun turnOffPlayer() {
        musicalPlayer.turnOffPlayer()
    }

    // Фабрика для создания ViewModel с пробросом Activity в конструктор
    companion object {
        fun getFactory(app: Application): ViewModelProvider.Factory {
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerVm::class.java)) {
                        return PlayerVm(application = app) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
            return factory

        }
    }
}