package com.playlistmaker.domain.usecase

interface MusicPlayerInteractor {
    fun preparePlayer()
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
}