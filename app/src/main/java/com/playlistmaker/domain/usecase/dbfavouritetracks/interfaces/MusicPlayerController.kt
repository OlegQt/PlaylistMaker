package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

interface MusicPlayerController {
    fun preparePlayer(musTrackUrl:String)
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
    fun getCurrentPos():Int
}