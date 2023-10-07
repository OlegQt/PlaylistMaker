package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.DeleteMusicTrackFromFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksUseCase
import kotlinx.coroutines.*
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

const val TRACK_DURATION_UPDATE_MILLIS = 300L

class PlayerVm(
    private val addToFavoriteUseCase: AddMusicTrackToFavouritesUseCase,
    private val loadFavouriteUseCase: LoadFavouriteTracksUseCase,
    private val deleteFavouriteUseCase: DeleteMusicTrackFromFavouritesUseCase
) : ViewModel() {
    //Job переменные для coroutines
    private var musicTracDurationUpdate: Job? = null

    // Переменная хранит значение времени проигранного трека
    private var playingTime = MutableLiveData<Long>()

    // Состояние плеера
    private var playerState = MutableLiveData<PlayerState>()

    // Текущий загруженный трек
    private val currentPlayingMusTrack = MutableLiveData<MusicTrack>()

    // Error message
    private val _errorMsg = MutableLiveData<String>()

    // Геттеры для lifeData
    val getPlayerState = playerState as LiveData<PlayerState>
    val getCurrentMusTrack = this.currentPlayingMusTrack as LiveData<MusicTrack>
    val getPlayingTime = this.playingTime as LiveData<Long>
    val errorMsg = this._errorMsg as LiveData<String>

    // Создаем instance музыкального плеера через KOIN, в конструктор передаем объект типа
    // функционального интерфейса -> При изменении состояния плеера меняем текущее состояние activity
    // через изменение соответствующей liveData
    private val musicalPlayer: MusicPlayerController = getKoin().get() {
        parametersOf(object : OnPlayerStateListener {
            override fun playerStateChanged(state: PlayerState) {
                playerState.postValue(state)
            }
        })
    }

    // Запускается при переходе на экран плеера
    // в случае, если intent не был пуст.
    // Допускается перемещение в блок init
    fun loadCurrentMusicTrack(trackToPlay: MusicTrack) {
        this.currentPlayingMusTrack.postValue(trackToPlay)
        preparePlayer(trackToPlay.previewUrl)
    }

    private fun updatePlayingTime() {
        playingTime.postValue(musicalPlayer.getCurrentPos().toLong())
    }

    fun trackDurationProvider() {
        musicTracDurationUpdate = viewModelScope.launch {
            while (playerState.value == PlayerState.STATE_PLAYING) {
                delay(TRACK_DURATION_UPDATE_MILLIS)
                updatePlayingTime()
            }
        }
    }

    private fun preparePlayer(musTrackUrl: String) {
        musicalPlayer.preparePlayer(musTrackUrl)
    }

    fun pushAddToFavourite() {
        if (currentPlayingMusTrack.value?.isFavourite == true) {
            // Если трек уже есть в базе, удаляем
            deleteTrackFromFavourites()
        } else {
            // Если трека в базе данных нет, добавляем
            addTrackToFavourite()
        }

        // Изменяем рисунок кнопки на противоположенный, перезаписывая значение LiveData track
        currentPlayingMusTrack.value = currentPlayingMusTrack.value?.apply {
            isFavourite = !isFavourite
        }
        //currentPlayingMusTrack.value?.copy(isFavourite = !currentPlayingMusTrack.value?.isFavourite!!)
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
                musicalPlayer.playMusic()
            }

            false -> {
                // Останавливаем воспроизведение музыки
                musicalPlayer.pauseMusic()
            }
        }
    }

    fun turnOffPlayer() {
        musicalPlayer.turnOffPlayer()
        musicTracDurationUpdate?.cancel()
        musicTracDurationUpdate = null
    }

    private fun addTrackToFavourite() {
        // Добавляем трек в базу сразу при старте плеера временно
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            if (currentPlayingMusTrack.value != null) {
                addToFavoriteUseCase.execute(currentPlayingMusTrack.value!!)
            }
        }
    }

    private fun deleteTrackFromFavourites(){
        viewModelScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                _errorMsg.value = throwable.message
            }
        ) {
            deleteFavouriteUseCase.execute(currentPlayingMusTrack.value!!)
        }

    }

    fun showFavTracks() {
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler) {
            val result = loadFavouriteUseCase.execute()
            result.collect() {
                val strBld = StringBuilder()
                it.forEach {
                    strBld.append("${it.trackName}   ${it.trackId} \n")
                }
                _errorMsg.postValue(strBld.toString())
            }
        }
    }

}