package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.MusicTrack

interface MusicTrackRepository {
    fun getCurrentMusicTrack():MusicTrack?
    fun setCurrentMusicTrack(track:MusicTrack)
}