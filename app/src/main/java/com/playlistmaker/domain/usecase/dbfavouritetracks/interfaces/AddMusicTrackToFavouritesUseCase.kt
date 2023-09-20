package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

import com.playlistmaker.domain.models.MusicTrack

interface AddMusicTrackToFavouritesUseCase {
    suspend fun execute(musicTrack: MusicTrack)
}