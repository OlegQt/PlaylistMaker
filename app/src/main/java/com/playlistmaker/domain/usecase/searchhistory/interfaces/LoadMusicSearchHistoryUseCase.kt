package com.playlistmaker.domain.usecase.searchhistory.interfaces

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.util.Resource

interface LoadMusicSearchHistoryUseCase {
    fun execute():Resource<ArrayList<MusicTrack>>
}