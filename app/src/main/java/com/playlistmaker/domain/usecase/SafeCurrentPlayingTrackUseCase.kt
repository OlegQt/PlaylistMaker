package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicTrackRepository

class SafeCurrentPlayingTrackUseCase (private val musicTrackRepository: MusicTrackRepository){
    fun execute(musicTrack:MusicTrack) {
        musicTrackRepository.setCurrentMusicTrack(track = musicTrack)
    }
}