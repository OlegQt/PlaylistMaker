package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto

interface MusicInteractor {
    fun searchMusic(songName: String, consumer: MusicConsumer)

    interface MusicConsumer {
        fun consume(foundMusic: ArrayList<MusicTrackDto>)
    }
}