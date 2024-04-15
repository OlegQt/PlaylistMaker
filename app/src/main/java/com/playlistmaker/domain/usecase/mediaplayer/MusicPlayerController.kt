package com.playlistmaker.domain.usecase.mediaplayer

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.presentation.ui.musicservice.MusicPlayerState
import kotlinx.coroutines.flow.StateFlow

interface MusicPlayerController {
    fun initialisePlayer(trackToPlay: MusicTrack)
    fun playPauseMusic()
    fun releasePlayerResources()
    fun showForegroundNotification(trackName: String, trackSinger: String)
    fun hideForegroundNotification()
    fun getCurrentPlayerState():StateFlow<MusicPlayerState>
}