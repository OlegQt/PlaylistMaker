package com.playlistmaker.domain.usecase.dbfavouritetracks

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces.LoadFavouriteTracksIdsUseCase
import kotlinx.coroutines.flow.Flow

class LoadFavouriteTracksIdsUseCaseImpl(private val favouriteTracksRepo: FavouriteMusicRepository):
    LoadFavouriteTracksIdsUseCase {
    override fun execute(): Flow<List<Long>> = favouriteTracksRepo.loadFavouriteTracksIds()
}