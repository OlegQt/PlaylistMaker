package com.playlistmaker.domain.usecase.mediaplayer

import com.playlistmaker.domain.models.MusicTrack

interface MusicPlayerController {
    fun initialisePlayer(trackToPlay: MusicTrack)
    fun playPauseMusic()
    fun releasePlayerResources()
    fun showForegroundNotification(trackName: String, trackSinger: String)
    fun hideForegroundNotification()
}