package com.playlistmaker.domain.usecase.dbfavouritetracks

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksUseCase
import kotlinx.coroutines.flow.Flow

class LoadFavouriteTracksUseCaseImpl(private val favouriteTracksRepo: FavouriteMusicRepository) :
    LoadFavouriteTracksUseCase {
    override fun execute(): Flow<List<MusicTrack>> = favouriteTracksRepo.loadFavouriteTracks()
}