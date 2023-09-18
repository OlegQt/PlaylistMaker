package com.playlistmaker.domain.usecase.dbfavourite

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack

class DeleteMusicTrackFromFavouritesUseCase(private val favouriteTracksRepo: FavouriteMusicRepository) {
    suspend fun execute(musicTrack: MusicTrack) {
        favouriteTracksRepo.deleteTrackFromFavourite(musicTrack)
    }
}