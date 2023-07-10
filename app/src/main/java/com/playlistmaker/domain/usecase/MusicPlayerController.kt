package com.playlistmaker.domain.usecase

import com.playlistmaker.data.playerimpl.MusicPlayerControllerImpl

interface MusicPlayerController {
    fun preparePlayer(musTrackUrl:String)
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
}