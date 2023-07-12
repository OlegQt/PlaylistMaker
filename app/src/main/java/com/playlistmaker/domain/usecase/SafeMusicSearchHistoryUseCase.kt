package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository

class SafeMusicSearchHistoryUseCase(private val musicRepository:MusicRepository) {
    fun execute(musicList:ArrayList<MusicTrack>){
        musicRepository.safeMusicSearchHistory(musicList)
    }
}