package com.playlistmaker.Logic

import android.media.MediaPlayer
import android.widget.Toast
import com.playlistmaker.Theme.App
import java.io.IOException

class Player( private var playerStateListener: OnPlayerStateListener?) {
    constructor():this(null)


    private val mediaPlayer: MediaPlayer = MediaPlayer()
    var currentState : Int = PlayerState.STATE_DEFAULT


    init {
        mediaPlayer.setOnPreparedListener {
            playerStateListener?.playerStateChanged(STATE_PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            // Register a callback to be invoked when the end of a media source has been reached during playback.
            it.seekTo(0)
            playerStateListener?.playerStateChanged(STATE_COMPLETE)
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

    fun playMusic() {
        mediaPlayer.start()
        playerStateListener?.playerStateChanged(STATE_PLAYING)
    }

    fun getDuration() = mediaPlayer.currentPosition

    fun pausePlayer() {
        mediaPlayer.pause()
        playerStateListener?.playerStateChanged(STATE_PAUSED)
    }

    fun turnOffPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        currentState = STATE_DEFAULT
    }

    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    fun interface OnPlayerStateListener {
        fun playerStateChanged(state: Int)
    }

    companion object PlayerState {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETE = 4
    }
}