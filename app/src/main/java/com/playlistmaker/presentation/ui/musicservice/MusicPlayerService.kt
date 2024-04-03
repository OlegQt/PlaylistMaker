package com.playlistmaker.presentation.ui.musicservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import com.playlistmaker.appstart.App
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayerService : Service() {

    // Binder given to clients.
    private val binder = LocalBinder()

    val _playerState = MutableStateFlow<MusicPlayerState>(MusicPlayerState.MusicPaused(0))
    val playerState = _playerState.asStateFlow()

    private val player = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder {

        getParcelableVersionIndependent(
            intent,
            App.MUSIC_PLAYER_SERVICE_TRACK_MODEL,
            MusicTrack::class.java
        )?.let { initialisePlayer(it) }

        return binder
    }

    override fun onDestroy() {
        Log.e("LOG", "Destroy Service")

        releasePlayerResources()
        super.onDestroy()
    }

    private fun initialisePlayer(trackToPlay: MusicTrack) {
        player.setOnPreparedListener {
            _playerState.value = MusicPlayerState.MusicReadyToPlay()
        }

        player.setOnCompletionListener {
            _playerState.value = MusicPlayerState.MusicPlayingCompleted()
        }

        player.setDataSource(trackToPlay.previewUrl)
        player.prepareAsync()
    }

    fun playPauseMusic() {
        when (_playerState.value) {
            is MusicPlayerState.MusicPaused -> startPlayingMusic()
            is MusicPlayerState.MusicReadyToPlay -> startPlayingMusic()
            is MusicPlayerState.MusicPlaying -> stopPlayingMusic()
            else -> {}
        }
    }

    private fun startPlayingMusic() {
        player.start()
        _playerState.value = MusicPlayerState.MusicPlaying(0)
    }

    private fun stopPlayingMusic() {
        player.pause()
        _playerState.value = MusicPlayerState.MusicPaused(player.currentPosition)
    }

    private fun releasePlayerResources() {

    }

    private fun <T> getParcelableVersionIndependent(
        intent: Intent?,
        name: String,
        clazz: Class<T>
    ): T? where T : Parcelable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(name, clazz)
        } else {
            intent?.getParcelableExtra<T?>(name)
        }
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}