package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto

interface MusicRepository {
    fun searchMusic(songName:String):ArrayList<MusicTrackDto>
}