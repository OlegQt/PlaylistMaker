package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository

class DeleteMusicSearchHistoryUseCase (private val musicRepository: MusicRepository) {
    fun execute(){
        musicRepository.deleteAllMusicSearchHistory()
    }
}