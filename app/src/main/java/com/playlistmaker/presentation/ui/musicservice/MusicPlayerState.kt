package com.playlistmaker.presentation.ui.musicservice

sealed class MusicPlayerState(val playProgress: Int? = null) {
    class MusicPaused(private val currentProgress: Int) :
        MusicPlayerState(playProgress = currentProgress)

    class MusicPlaying(private val currentProgress: Int) :
        MusicPlayerState(playProgress = currentProgress)

}