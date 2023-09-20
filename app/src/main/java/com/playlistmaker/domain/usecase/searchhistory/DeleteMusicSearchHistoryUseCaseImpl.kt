package com.playlistmaker.domain.usecase.searchhistory

import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.usecase.searchhistory.interfaces.DeleteMusicSearchHistoryUseCase

class DeleteMusicSearchHistoryUseCaseImpl (private val musicRepository: MusicRepository):DeleteMusicSearchHistoryUseCase {
    override fun execute(){
        musicRepository.deleteAllMusicSearchHistory()
    }
}