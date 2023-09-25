package com.playlistmaker.domain.usecase.dbfavouritetracks.interfaces

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow

interface LoadFavouriteTracksUseCase {
    fun execute(): Flow<List<MusicTrack>>
}