package com.playlistmaker.domain.usecase

import android.media.MediaPlayer
import java.io.IOException

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
        } catch (e: IOException) {
            //
        }

        try {
            mediaPlayer.prepareAsync()
        } catch (e: IllegalStateException) {
            //
        } catch (e: NullPointerException) {
            //
        } catch (e: IOException) {
            //
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

    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    fun interface OnPlayerStateListener {
        fun playerStateChanged(state: PlayerState)
    }

    enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED,
        STATE_COMPLETE
    }
}