package com.playlistmaker.domain.usecase.dbfavourite

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack

class AddMusicTrackToFavouritesUseCase(private val favouriteTracksRepository: FavouriteMusicRepository) {
    fun execute(musicTrack: MusicTrack){
        favouriteTracksRepository.saveMusicTrackToFavourites(musicTrack)
    }
}