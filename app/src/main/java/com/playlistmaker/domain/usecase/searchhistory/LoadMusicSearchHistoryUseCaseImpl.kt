package com.playlistmaker.domain.usecase.searchhistory

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.usecase.searchhistory.interfaces.LoadMusicSearchHistoryUseCase
import com.playlistmaker.util.Resource

class LoadMusicSearchHistoryUseCaseImpl (private val musicRepository: MusicRepository):
    LoadMusicSearchHistoryUseCase {
    override fun execute():Resource<ArrayList<MusicTrack>>{
        return musicRepository.loadMusicSearchHistory()
    }
}