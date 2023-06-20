package com.playlistmaker.domain.api

import com.playlistmaker.data.dto.MusicTrackDto

interface MusicInteractor {
    fun searchMovies(songName: String, consumer: MusicConsumer)

    interface MusicConsumer {
        fun consume(foundMovies: ArrayList<MusicTrackDto>)
    }
}