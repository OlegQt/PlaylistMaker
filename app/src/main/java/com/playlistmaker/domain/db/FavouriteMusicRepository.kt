package com.playlistmaker.domain.db

import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow

interface FavouriteMusicRepository {
    fun loadFavouriteTracks():Flow<List<MusicTrack>>

    suspend fun saveMusicTrackToFavourites(musicTrack: MusicTrack)

    fun loadFavouriteTracksIds():Flow<List<Long>>

    suspend fun deleteTrackFromFavourite(musicTrack: MusicTrack)
}