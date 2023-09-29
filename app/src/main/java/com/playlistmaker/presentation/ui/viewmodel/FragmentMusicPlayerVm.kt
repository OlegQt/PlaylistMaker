package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

const val TRACK_DURATION_UPDATE_mills = 300L

class FragmentMusicPlayerVm(
    private val addToFavoriteUseCase: AddMusicTrackToFavouritesUseCase
) : ViewModel() {
    // Состояние плеера
    private var _playerState = MutableLiveData<PlayerState>()
    val playerState = _playerState as LiveData<PlayerState>

    private val _playingTime = MutableLiveData<Long>()
    val playingTime = _playingTime as LiveData<Long>

    private val musicalPlayer = getKoin().get<MusicPlayerController>()

    fun startTrackPlayingTimer(){
        viewModelScope.launch {
            while (_playerState.value == PlayerState.STATE_PLAYING) {
                delay(TRACK_DURATION_UPDATE_mills)
                updatePlayingTime()
            }
        }
    }

    fun loadCurrentMusicTrack(trackToPlay: MusicTrack) {
        musicalPlayer.setMusicPlayerStateListener { _playerState.postValue(it) }
        musicalPlayer.preparePlayer(musTrackUrl = trackToPlay.previewUrl)
    }

    fun pushPlayPauseButton() {
        when (_playerState.value) {
            PlayerState.STATE_PLAYING -> playPauseMusic(false)
            PlayerState.STATE_PAUSED -> playPauseMusic(true)
            PlayerState.STATE_PREPARED -> playPauseMusic(true)
            else -> {}
        }
    }

    private fun playPauseMusic(isPlaying: Boolean) {
        if (isPlaying) {
            musicalPlayer.playMusic()
        } else {
            musicalPlayer.pauseMusic()
        }
    }

    private fun updatePlayingTime() {
        _playingTime.value = musicalPlayer.getCurrentPos().toLong()
    }

}