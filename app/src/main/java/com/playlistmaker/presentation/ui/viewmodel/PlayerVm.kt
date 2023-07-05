package com.playlistmaker.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playlistmaker.data.repository.MusicTrackRepositoryImpl
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.MusicPlayerController
import com.playlistmaker.domain.usecase.MusicPlayerControllerImpl

class PlayerVm(private val application: Application) : AndroidViewModel(application) {

    private val musTrackRepo = MusicTrackRepositoryImpl(application.baseContext)

    private var playerState =
        MutableLiveData<MusicPlayerControllerImpl.PlayerState>() // Состояние плеера
    private val currentPlayingMusTrack = MutableLiveData<MusicTrack>() // Текущий загруженный трек

    // Геттеры для lifeData
    val getPlayerState = playerState as LiveData<MusicPlayerControllerImpl.PlayerState>
    val getCurrentMusTrack = this.currentPlayingMusTrack as LiveData<MusicTrack>

    // Переменная для хранения нашего плеера
    // В параметры передается объект реализующий функциональный интерфейс по обновлению состояния плеера
    private val musicalPlayer = MusicPlayerControllerImpl() { this.playerState.postValue(it) }

    // Запускается при переходе на экран плеера
    // допускается перемещение в блок init
    fun loadCurrentMusicTrack() {
        val musTrack = musTrackRepo.getCurrentMusicTrack()
        if (musTrack != null) this.currentPlayingMusTrack.postValue(musTrack)
    }

    // Запускается в observer на getCurrentMusTrack
    // Функция будет запущена только при наличии трека в переменной getCurrentMusTrack
    fun preparePlayer(musTrackUrl: String) {
        musicalPlayer.preparePlayer(musTrackUrl)
    }

    fun playOrPauseMusic() {
        when (playerState.value) {
            MusicPlayerControllerImpl.PlayerState.STATE_PLAYING -> musicalPlayer.pauseMusic()
            MusicPlayerControllerImpl.PlayerState.STATE_PAUSED -> musicalPlayer.playMusic()
            MusicPlayerControllerImpl.PlayerState.STATE_PREPARED -> musicalPlayer.playMusic()
            MusicPlayerControllerImpl.PlayerState.STATE_COMPLETE -> musicalPlayer.playMusic()
            else -> {}
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