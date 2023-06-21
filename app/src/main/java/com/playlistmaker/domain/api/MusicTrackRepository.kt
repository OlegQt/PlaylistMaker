package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto

interface MusicTrackRepository {
    fun getCurrentMusicTrack():MusicTrackDto?
    fun setCurrentMusicTrack(track:MusicTrackDto)
}