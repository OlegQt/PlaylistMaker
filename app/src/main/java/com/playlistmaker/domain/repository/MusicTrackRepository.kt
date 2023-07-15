package com.playlistmaker.domain.repository

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource

interface MusicTrackRepository {
    fun getCurrentMusicTrack():Resource<MusicTrack>
    fun setCurrentMusicTrack(track:MusicTrack)
}