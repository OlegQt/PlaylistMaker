package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

import com.playlistmaker.domain.db.FavouriteMusicRepository
import kotlinx.coroutines.flow.Flow

interface LoadFavouriteTracksIdsUseCase{
    fun execute(): Flow<List<Long>>
}