package com.playlistmaker.domain.usecase

interface MusicPlayerController {
    fun preparePlayer(musTrackUrl:String)
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
}