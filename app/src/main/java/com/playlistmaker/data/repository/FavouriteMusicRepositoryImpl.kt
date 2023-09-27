package com.playlistmaker.data.repository

import com.playlistmaker.data.db.favourite.MusicDB
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.db.FavouriteMusicRepository
import com.playlistmaker.domain.models.MusicTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteMusicRepositoryImpl(
    private val db: MusicDB,
    private val mapper: MusicTrackMapper
) : FavouriteMusicRepository {

    // Функция загружает id всех треков, находящихся в базе данных
    override fun loadFavouriteTracksIds(): Flow<List<Long>> =
        flow { emit(db.musicDao().getAllTracksId()) }

    override suspend fun deleteTrackFromFavourite(musicTrack: MusicTrack) {
        db.musicDao().deleteFavouriteTrack(mapper.mapToDao(musicTrack))
    }

    // Функция загружает все треки, находящиеся в базе данных
    override fun loadFavouriteTracks(): Flow<List<MusicTrack>> {
        return flow<List<MusicTrack>> {
            val favouriteTracks = db.musicDao().readAllMusicFromDb()
            val tracks = favouriteTracks.map { mapper.mapFromDao(it) }
            emit(tracks)
        }
    }

    // Добавляет трек в базу данных
    override suspend fun saveMusicTrackToFavourites(musicTrack: MusicTrack) {
        db.musicDao().addTrackToFavourite(mapper.mapToDao(musicTrack))
    }
}