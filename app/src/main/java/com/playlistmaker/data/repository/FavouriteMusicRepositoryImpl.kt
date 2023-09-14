package com.playlistmaker.data.repository

import com.playlistmaker.data.db.MusicDB
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteMusicRepositoryImpl(
    private val db: MusicDB,
    private val mapper: MusicTrackMapper
) : FavouriteMusicRepository {
    override fun loadFavouriteTracks(): Flow<List<MusicTrack>> {
        return flow<List<MusicTrack>> {
            val favouriteTracks = db.musicDao().readAllMusicFromDb()
            val tracks = favouriteTracks.map { mapper.mapFromDao(it) }
            emit(tracks)
        }
    }
}