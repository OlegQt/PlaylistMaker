package com.playlistmaker.domain.impl

import com.playlistmaker.domain.api.MusicInteractor
import com.playlistmaker.domain.api.MusicRepository

class MusicInteractorImpl(private val musicRepository: MusicRepository):MusicInteractor {

    private val executor = java.util.concurrent.Executors.newCachedThreadPool()

    override fun searchMovies(songName: String, consumer: MusicInteractor.MusicConsumer) {
        executor.execute {
            consumer.consume(musicRepository.searchMusic(songName))
        }
    }
}