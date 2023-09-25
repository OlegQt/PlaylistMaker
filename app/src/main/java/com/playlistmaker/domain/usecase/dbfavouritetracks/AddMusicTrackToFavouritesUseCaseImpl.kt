package com.playlistmaker.domain.usecase.dbfavouritetracks

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.AddMusicTrackToFavouritesUseCase

class AddMusicTrackToFavouritesUseCaseImpl(private val favouriteTracksRepository: FavouriteMusicRepository):
    AddMusicTrackToFavouritesUseCase {
    override suspend fun execute(musicTrack: MusicTrack){
        favouriteTracksRepository.saveMusicTrackToFavourites(musicTrack)
    }
}