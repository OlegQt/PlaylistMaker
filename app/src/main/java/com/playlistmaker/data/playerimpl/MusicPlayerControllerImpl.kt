package com.playlistmaker.data.playerimpl
import android.media.MediaPlayer
import com.playlistmaker.domain.models.OnPlayerStateListener
import com.playlistmaker.domain.models.PlayerState
import com.playlistmaker.domain.usecase.MusicPlayerController

class MusicPlayerControllerImpl(private val listener: OnPlayerStateListener) :
    MusicPlayerController {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentState = PlayerState.STATE_DEFAULT



    init {
        mediaPlayer.setOnPreparedListener {
            listener.playerStateChanged(PlayerState.STATE_PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            // Register a callback to be invoked when the end of a media source
            // has been reached during playback.
            it.seekTo(0)
            listener.playerStateChanged(PlayerState.STATE_COMPLETE)
        }
    }


    override fun preparePlayer(musTrackUrl:String) {
        try {
            mediaPlayer.setDataSource(musTrackUrl)
            mediaPlayer.prepareAsync()
        } catch (t: Throwable) {
            // The base class for all errors and exceptions
        }
    }

    override fun playMusic() {
        mediaPlayer.start()
        listener.playerStateChanged(PlayerState.STATE_PLAYING)
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        listener.playerStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun turnOffPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        currentState = PlayerState.STATE_DEFAULT
    }

    fun getCurrentPos() = mediaPlayer.currentPosition

}