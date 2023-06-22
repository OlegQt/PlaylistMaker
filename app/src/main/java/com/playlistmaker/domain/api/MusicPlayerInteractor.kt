package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto

interface MusicPlayerInteractor {
    fun preparePlayer()
    fun playMusic()
    fun pauseMusic()
    fun turnOffPlayer()
}