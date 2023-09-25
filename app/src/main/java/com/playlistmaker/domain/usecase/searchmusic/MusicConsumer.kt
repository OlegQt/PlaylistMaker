package com.playlistmaker.domain.usecase.searchmusic

import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.util.Resource

fun interface MusicConsumer {
    fun consume(foundMovies: Resource<ArrayList<MusicTrack>>)
}