package com.playlistmaker.data.db.favourite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MusicDao {
    // Add new musicTrack to DB
    @Insert(entity = MusicTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavourite(musTrack: MusicTrackEntity)

    // Read all track from DB
    @Query("SELECT * from FavouriteTracks ORDER BY insert_time DESC")
    suspend fun readAllMusicFromDb(): List<MusicTrackEntity>

    // Delete all tracks
    @Query("DELETE from FavouriteTracks")
    suspend fun clearFavouriteTracks()

    // Функция читает из базы список id из всех треков
    @Query("SELECT id from FavouriteTracks")
    suspend fun getAllTracksId(): List<Long>

    @Delete(entity = MusicTrackEntity::class)
    suspend fun deleteFavouriteTrack(musicTrack: MusicTrackEntity)
}