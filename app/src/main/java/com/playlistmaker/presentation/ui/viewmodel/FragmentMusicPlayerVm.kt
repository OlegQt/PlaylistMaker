package com.playlistmaker.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayList
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.DeleteMusicTrackFromFavouritesUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksUseCase
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.MusicPlayerController
import com.playlistmaker.domain.usecase.dbplaylist.PlayListController
import com.playlistmaker.presentation.models.FragmentPlaylistsState
import com.playlistmaker.presentation.ui.musicservice.MusicPlayerService
import com.playlistmaker.presentation.ui.musicservice.MusicPlayerState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

const val TRACK_DURATION_UPDATE_mills = 300L

class FragmentMusicPlayerVm(
    private val musicalPlayer: MusicPlayerController,
    private val addToFavoriteUseCase: AddMusicTrackToFavouritesUseCase,
    private val loadFavouriteUseCase: LoadFavouriteTracksUseCase,
    private val deleteFavouriteUseCase: DeleteMusicTrackFromFavouritesUseCase,
    private val playListController: PlayListController
) : ViewModel() {
    // Состояние плеера
    private var _playerState = MutableLiveData<PlayerState>()
    val playerState = _playerState as LiveData<PlayerState>

    private val _playingTime = MutableLiveData<Long>()
    val playingTime = _playingTime as LiveData<Long>

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg = _errorMsg as LiveData<String>

    private val _currentPlayingMusTrack = MutableLiveData<MusicTrack>()
    val currentMusTrack = _currentPlayingMusTrack as LiveData<MusicTrack>

    private val _bottomSheetIsShown = MutableLiveData<Boolean>()
    val bottomSheetIsShown = _bottomSheetIsShown as LiveData<Boolean>

    private val _playlistState =
        MutableStateFlow<FragmentPlaylistsState>(FragmentPlaylistsState.NothingFound(null))
    val playlistState = _playlistState as StateFlow<FragmentPlaylistsState>

    private var trackPlayingTimerListener: Job? = null

    private var musicServiceRef:WeakReference<MusicPlayerService?>? = null
    private val musicService:MusicPlayerService? get() = musicServiceRef?.get()

    val mState = musicService?._playerState?.asStateFlow()

    init {
        // Flow collecting the playLists list from dataBase
        updateListOfPlaylistFromDB()
    }

    fun setNewMusicPlayerService(newPlayer: MusicPlayerService) {
        //TODO: new fun, which replaces internal player with service player
        musicServiceRef = WeakReference(newPlayer)
        //musicService?.startPlayingMusic()
    }

    fun loadCurrentMusicTrack(trackToPlay: MusicTrack) {
        musicalPlayer.setMusicPlayerStateListener { _playerState.postValue(it) }
        musicalPlayer.preparePlayer(musTrackUrl = trackToPlay.previewUrl)
        _currentPlayingMusTrack.value = trackToPlay
    }

    fun pushPlayPauseButton() {
        Log.e("LOG", "PUSH BUTTON PLAY ${_playerState.value}")
        when (_playerState.value) {
            PlayerState.STATE_PLAYING -> playPauseMusic(false)
            PlayerState.STATE_PAUSED -> playPauseMusic(true)
            PlayerState.STATE_PREPARED -> playPauseMusic(true)
            PlayerState.STATE_COMPLETE -> playPauseMusic(true)
            else -> _errorMsg.value = "UncorrectedState"
        }
    }

    fun playPauseMusic(isPlaying: Boolean) {
        //if (isPlaying) musicalPlayer.playMusic()
        //else musicalPlayer.pauseMusic()

        //TODO: replace player controller with service
        musicService?.playPauseMusic()
    }

    fun stopTrackPlayingTimer() {
        trackPlayingTimerListener?.cancel()
    }

    fun startTrackPlayingTimer() {
        trackPlayingTimerListener = viewModelScope.launch {
            while (_playerState.value == PlayerState.STATE_PLAYING) {
                delay(TRACK_DURATION_UPDATE_mills)
                updatePlayingTime()
            }
        }
    }

    private fun updatePlayingTime() {
        _playingTime.value = musicalPlayer.getCurrentPos().toLong()
    }

    fun turnOffPlayer() {
        trackPlayingTimerListener?.cancel()
        trackPlayingTimerListener = null

        musicalPlayer.turnOffPlayer()

        // В случае вызова функции onPause фрагмента при данном state действия с
        // плеером не будут производиться
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    fun pushAddToFavButton() {
        _currentPlayingMusTrack.value?.let {
            if (it.isFavourite) deleteTrackFromFavourite(it)
            else addTrackToFavourite(it)

            _currentPlayingMusTrack.value = it.copy(isFavourite = !it.isFavourite)
        }
    }

    private fun addTrackToFavourite(musicTrack: MusicTrack) {
        // Добавляем трек в базу сразу при старте плеера временно
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            addToFavoriteUseCase.execute(musicTrack)
        }
    }

    private fun deleteTrackFromFavourite(musicTrack: MusicTrack) {
        // Добавляем трек в базу сразу при старте плеера временно
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler + Dispatchers.IO) {
            deleteFavouriteUseCase.execute(musicTrack)
        }
    }

    fun showAllFavouriteTracks(): Boolean {
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _errorMsg.value = throwable.message
        }
        viewModelScope.launch(errorHandler) {
            val result = loadFavouriteUseCase.execute()
            result.collect {
                val strBld = StringBuilder()
                it.forEach {
                    strBld.append("${it.trackName}   ${it.trackId} \n")
                }
                _errorMsg.postValue(strBld.toString())
            }
        }
        return true
    }

    fun onPlayListClick(clickedPlayList: PlayList) {
        currentMusTrack.value?.let {
            //Заходим на обновление плейлистов с проверкой трека на null
            if (clickedPlayList.trackList.isEmpty()) {
                addFirstTrackToPlaylist(trackToSave = it, toPlayList = clickedPlayList)
                saveMusicTrackInTrackDB(musicTrack = it)
            } else {
                if (isTrackAlreadyInPlaylist(trackToFind = it, inPlayList = clickedPlayList)) {
                    _errorMsg.value = "Трек уже добавлен в плейлист ${clickedPlayList.name}"
                } else {
                    addTrackToPlaylist(trackToSave = it, toPlayList = clickedPlayList)
                    saveMusicTrackInTrackDB(musicTrack = it)
                }
            }
        }
    }

    private fun saveMusicTrackInTrackDB(musicTrack: MusicTrack) {
        viewModelScope.launch {
            playListController.safeMusicTrackToTrackListDB(musicTrackToSafe = musicTrack)
            // Если текущий трек не добавлен в выбранный плейлист, то окно добавления трека в плейлист исчезает,
            // текущий трек добавляется в выбранный плейлист
            _bottomSheetIsShown.value = false
        }
    }

    private fun addFirstTrackToPlaylist(trackToSave: MusicTrack, toPlayList: PlayList) {
        val newJsonIdString = Gson().toJson(arrayOf(trackToSave.trackId))

        viewModelScope.launch {
            playListController.updatePlayList(
                toPlayList.copy(
                    trackList = newJsonIdString,
                    quantity = 1
                )
            )
            _errorMsg.value = "Добавлено в плейлист ${toPlayList.name}"
        }
    }

    private fun addTrackToPlaylist(trackToSave: MusicTrack, toPlayList: PlayList) {
        // Считываем id всех треков из строки в массив
        val data: Array<Long> = Gson().fromJson(toPlayList.trackList, Array<Long>::class.java)

        // Копируем в новый массив и добавляем id музыкального трека
        val newArray = data.plus(trackToSave.trackId)

        // Переводим снова в строку типа JSON
        val newJsonIdString = Gson().toJson(newArray)

        viewModelScope.launch {
            playListController.updatePlayList(
                toPlayList.copy(
                    trackList = newJsonIdString,
                    quantity = newArray.size
                )
            )
            _errorMsg.value = "Добавлено в плейлист ${toPlayList.name}"
        }
    }

    private fun isTrackAlreadyInPlaylist(trackToFind: MusicTrack, inPlayList: PlayList): Boolean {
        val data: Array<Long> = Gson().fromJson(inPlayList.trackList, Array<Long>::class.java)
        return data.contains(trackToFind.trackId)
    }

    private fun updateListOfPlaylistFromDB() {
        viewModelScope.launch {
            playListController.loadAllPlayLists().collect {
                _playlistState.value = FragmentPlaylistsState.Content(it)
            }
        }
    }

}