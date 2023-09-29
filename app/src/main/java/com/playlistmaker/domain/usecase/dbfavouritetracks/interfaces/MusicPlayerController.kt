package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

import com.playlistmaker.domain.models.OnPlayerStateListener
import kotlinx.coroutines.flow.Flow

interface MusicPlayerController {
    fun setMusicPlayerStateListener(actualListener: OnPlayerStateListener)
    fun preparePlayer(musTrackUrl:String)
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
    fun getCurrentPos():Int
}