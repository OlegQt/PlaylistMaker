package com.playlistmaker.domain.usecase.dbfavouritetracks

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.DeleteMusicTrackFromFavouritesUseCase

class DeleteMusicTrackFromFavouritesUseCaseImpl(private val favouriteTracksRepo: FavouriteMusicRepository) :
    DeleteMusicTrackFromFavouritesUseCase {
    override suspend fun execute(musicTrack: MusicTrack) {
        favouriteTracksRepo.deleteTrackFromFavourite(musicTrack)
    }
}