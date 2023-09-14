package com.playlistmaker.domain.usecase

import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow

class LoadFavouriteTracksUseCase(private val favouriteTracksRepo:FavouriteMusicRepository) {
    fun execute():Flow<List<MusicTrack>> = favouriteTracksRepo.loadFavouriteTracks()
}