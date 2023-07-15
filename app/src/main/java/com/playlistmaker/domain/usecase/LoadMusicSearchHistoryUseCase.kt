package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.util.Resource

class LoadMusicSearchHistoryUseCase (private val musicRepository: MusicRepository) {
    fun execute():Resource<ArrayList<MusicTrack>>{
        return musicRepository.loadMusicSearchHistory()
    }
}