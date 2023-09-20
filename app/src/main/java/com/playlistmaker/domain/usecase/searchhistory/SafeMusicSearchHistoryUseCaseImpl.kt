package com.playlistmaker.domain.usecase.searchhistory

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository
import com.playlistmaker.domain.usecase.searchhistory.interfaces.SafeMusicSearchHistoryUseCase

class SafeMusicSearchHistoryUseCaseImpl(private val musicRepository: MusicRepository) :
    SafeMusicSearchHistoryUseCase {
    override fun execute(musicList: ArrayList<MusicTrack>) {
        musicRepository.safeMusicSearchHistory(musicList)
    }
}