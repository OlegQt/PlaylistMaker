package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.util.Resource

class LoadLastPlayingMusicTrackUseCase(private val musicTrackRepository: MusicTrackRepository) {
    fun execute(): Resource<MusicTrack> {
        return musicTrackRepository.getCurrentMusicTrack()
    }
}