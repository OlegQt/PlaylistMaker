package com.playlistmaker.Logic

import android.media.MediaPlayer
import android.widget.Toast
import com.playlistmaker.Theme.App
import java.io.IOException

class Player() {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerStateListener: onPlayerStateListener? = null
    private var currentState = STATE_DEFAULT

    init {
        mediaPlayer.setOnPreparedListener {
            currentState = STATE_PREPARED
            playerStateListener?.playerStateChanged(currentState)
        }

        mediaPlayer.setOnCompletionListener {
            // Register a callback to be invoked when the end of a media source has been reached during playback.
            currentState = STATE_PREPARED
            playerStateListener?.playerStateChanged(currentState)
        }
    }

    fun setOnPlayerStateListener(listener: onPlayerStateListener) {
        this.playerStateListener = listener
    }


    fun preparePlayer(songUrl: String) {
        var url =
            "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/ac/c7/d1/acc7d13f-6634-495f-caf6-491eccb505e8/mzaf_4002676889906514534.plus.aac.p.m4a"
        try {
            mediaPlayer.setDataSource(url)
        } catch (e: IOException) {
            Toast.makeText(App.instance, "${e.message}", Toast.LENGTH_SHORT).show()
        }

        try {
            mediaPlayer.prepareAsync()
        } catch (e: IllegalStateException) {
            Toast.makeText(App.instance, "illegal ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: NullPointerException) {
            Toast.makeText(App.instance, "NullPointer ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(App.instance, "IO ${e.message}", Toast.LENGTH_SHORT).show()
        }


    }

    fun startPlayer() {
        mediaPlayer.start()
        currentState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        currentState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (currentState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun turnOffPlayer() {
        mediaPlayer.release()
        currentState = STATE_DEFAULT
    }

    fun interface onPlayerStateListener {
        fun playerStateChanged(state: Int)
    }

    companion object PlayerState {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}