package com.playlistmaker.presentation.ui.viewmodel

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.LoadLastPlayingMusicTrackUseCase
import com.playlistmaker.domain.usecase.MusicPlayerController
import com.playlistmaker.util.Resource
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin


class PlayerVm(private val loadLastPlayingTrackUseCase: LoadLastPlayingMusicTrackUseCase) :
    ViewModel() {
    private val handler = android.os.Handler(Looper.getMainLooper())

    private var playingTime = MutableLiveData<Long>()
    private var playerState =
        MutableLiveData<PlayerState>() // Состояние плеера
    private val currentPlayingMusTrack = MutableLiveData<MusicTrack>() // Текущий загруженный трек

    // Геттеры для lifeData
    val getPlayerState = playerState as LiveData<PlayerState>
    val getCurrentMusTrack = this.currentPlayingMusTrack as LiveData<MusicTrack>
    val getPlayingTime = this.playingTime as LiveData<Long>

    // Создаем инстанс музыкального плеера через KOIN, в конструктор передаем объект типа
    // функционального интерфейса
    private val musicalPlayer: MusicPlayerController = getKoin().get() {
        parametersOf(object : OnPlayerStateListener {
            override fun playerStateChanged(state: PlayerState) {
                playerState.postValue(state)
            }
        })
    }

    // Запускается при переходе на экран плеера
    // допускается перемещение в блок init
    fun loadCurrentMusicTrack() {
        val loadResult = loadLastPlayingTrackUseCase.execute()
        if (loadResult is Resource.Success) {
            this.currentPlayingMusTrack.postValue(loadResult.data)
        }
    }

    private fun updatePlayingTime() {
        playingTime.postValue(musicalPlayer.getCurrentPos().toLong())
        handler.postDelayed({ updatePlayingTime() }, 100)

    }

    private fun startTimer() {
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

    fun playPauseMusic(play: Boolean) {
        when (play) {
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
}