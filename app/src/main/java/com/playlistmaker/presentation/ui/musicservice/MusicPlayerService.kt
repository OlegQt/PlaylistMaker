package com.playlistmaker.presentation.ui.musicservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import com.playlistmaker.R
import com.playlistmaker.appstart.App
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.mediaplayer.MusicPlayerController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.Koin

class MusicPlayerService : Service() ,MusicPlayerController{

    // Binder given to clients.
    private val binder = LocalBinder()

    private val _playerState = MutableStateFlow<MusicPlayerState>(MusicPlayerState.PlayerLoad())
    val playerState = _playerState.asStateFlow()

    private var playerProgressUpdateJob: Job? = null

    private val player:MediaPlayer = get()

    override fun onBind(intent: Intent?): IBinder {

        getParcelableVersionIndependent(
            intent,
            App.MUSIC_PLAYER_SERVICE_TRACK_MODEL,
            MusicTrack::class.java
        )?.let { initialisePlayer(it) }

        return binder
    }

    override fun onDestroy() {
        releasePlayerResources()
        super.onDestroy()
    }

    override fun initialisePlayer(trackToPlay: MusicTrack) {
        player.setOnPreparedListener {
            _playerState.value = MusicPlayerState.MusicReadyToPlay()
        }

        player.setOnCompletionListener {
            // StopForeground and remove notification
            hideForegroundNotification()

            // Go to musicTrack start position and pause player manually
            player.seekTo(0)
            player.pause()

            // Stop coroutine watching progress and send new State to Fragment
            stopProgressUpdate()
            _playerState.value = MusicPlayerState.MusicPaused(player.currentPosition)
        }

        player.setDataSource(trackToPlay.previewUrl)
        player.prepareAsync()
    }

    override fun playPauseMusic() {
        when (_playerState.value) {
            is MusicPlayerState.MusicPaused -> startPlayingMusic()
            is MusicPlayerState.MusicReadyToPlay -> startPlayingMusic()
            is MusicPlayerState.MusicPlaying -> stopPlayingMusic()
            else -> {}
        }
    }

    private fun startPlayingMusic() {
        // If player is not yet prepared -> return
        if (_playerState.value is MusicPlayerState.PlayerLoad) return

        // else start playing music
        player.start()
        startProgressUpdate()
    }

    private fun stopPlayingMusic() {
        player.pause()
        stopProgressUpdate()
        _playerState.value = MusicPlayerState.MusicPaused(player.currentPosition)
    }

    override fun releasePlayerResources() {
        stopPlayingMusic()

        player.stop()
        player.setOnPreparedListener(null)
        player.setOnCompletionListener(null)
        player.release()
    }

    private fun startProgressUpdate() {
        playerProgressUpdateJob = CoroutineScope(Dispatchers.Default).launch {
            while (player.isPlaying) {
                delay(App.MUSIC_PLAYER_PROGRESS_FREQUENCY)
                _playerState.value = MusicPlayerState.MusicPlaying(
                    currentProgress = player.currentPosition
                )
            }
        }
    }

    private fun stopProgressUpdate() {
        playerProgressUpdateJob?.cancel()
        playerProgressUpdateJob = null
    }

    override fun showForegroundNotification(trackName: String, trackSinger: String) {
        val notification =
            NotificationCompat.Builder(this, App.MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(trackSinger.plus(" - ").plus(trackName))
                .setSubText(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_media)
                .build()

        // Create the notification to display while the service is running
        // only if music is playing
        if (_playerState.value is MusicPlayerState.MusicPlaying) startForeground(100, notification)
    }

    override fun hideForegroundNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun getCurrentPlayerState(): StateFlow<MusicPlayerState> {
        return playerState
    }

    private inline fun <reified T> getParcelableVersionIndependent(
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