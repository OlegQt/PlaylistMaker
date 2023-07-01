package com.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.playlistmaker.domain.api.MusicPlayerInteractor
import com.playlistmaker.domain.api.MusicTrackRepository
import java.io.IOException

class MusicPlayerInteractorImpl(private val musTrackRepo:MusicTrackRepository,private val listener: MusicPlayerInteractorImpl.OnPlayerStateListener) :MusicPlayerInteractor{

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentState : Int = PlayerState.STATE_DEFAULT
    private var musicTrackUrl = musTrackRepo.getCurrentMusicTrack()?.previewUrl


    init {
        mediaPlayer.setOnPreparedListener {
            listener.playerStateChanged(STATE_PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            // Register a callback to be invoked when the end of a media source
            // has been reached during playback.
            it.seekTo(0)
            listener.playerStateChanged(STATE_COMPLETE)
        }
    }


    override fun preparePlayer() {
        try {
            mediaPlayer.setDataSource(musicTrackUrl)
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
        listener.playerStateChanged(STATE_PLAYING)
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        listener.playerStateChanged(STATE_PAUSED)
    }

    override fun turnOffPlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        currentState = STATE_DEFAULT
    }

    fun getCurrentPos() = mediaPlayer.currentPosition

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