package com.playlistmaker.domain.impl

import com.playlistmaker.Theme.App
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.domain.api.MusicTrackRepository

class MusicTrackRepositoryImpl(): MusicTrackRepository{
    override fun getCurrentMusicTrack(): MusicTrackDto? {
        return App.instance.loadCurrentPlayingTrack()
    }

    override fun setCurrentMusicTrack(track: MusicTrackDto) {
        //TODO("Not yet implemented")
    }
}