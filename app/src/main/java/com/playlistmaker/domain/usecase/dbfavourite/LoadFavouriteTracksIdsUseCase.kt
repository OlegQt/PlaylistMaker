package com.playlistmaker.domain.usecase.dbfavourite

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow

class LoadFavouriteTracksIdsUseCase(private val favouriteTracksRepo:FavouriteMusicRepository) {
    fun execute():Flow<List<Long>> = favouriteTracksRepo.loadFavouriteTracksIds()
}