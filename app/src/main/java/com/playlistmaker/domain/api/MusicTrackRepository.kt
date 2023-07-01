package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.domain.models.MusicTrack

interface MusicTrackRepository {
    fun getCurrentMusicTrack():MusicTrack?
    fun setCurrentMusicTrack(track:MusicTrack)
}