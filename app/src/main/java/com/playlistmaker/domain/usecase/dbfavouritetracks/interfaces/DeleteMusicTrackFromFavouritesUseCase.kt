package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack

interface DeleteMusicTrackFromFavouritesUseCase {
    suspend fun execute(musicTrack: MusicTrack)
}