package com.playlistmaker.Logic

import android.media.MediaPlayer
import android.widget.Toast
import com.playlistmaker.Theme.App
import java.io.IOException

class Player() {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerStateListener: OnPlayerStateListener? = null
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

    fun setOnPlayerStateListener(listener: OnPlayerStateListener) {
        this.playerStateListener = listener
    }


    fun preparePlayer(songUrl: String) {
        try {
            mediaPlayer.setDataSource(songUrl)
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

    fun interface OnPlayerStateListener {
        fun playerStateChanged(state: Int)
    }

    companion object PlayerState {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}