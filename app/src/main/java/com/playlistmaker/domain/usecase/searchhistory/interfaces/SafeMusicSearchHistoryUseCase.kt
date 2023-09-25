package com.playlistmaker.domain.usecase.searchhistory.interfaces

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicRepository

interface SafeMusicSearchHistoryUseCase{
    fun execute(musicList:ArrayList<MusicTrack>)
}