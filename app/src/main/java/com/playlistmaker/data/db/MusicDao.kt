package com.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MusicDao {
    @Insert(entity = MusicTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addTrackToFavourite(musTrack:MusicTrackEntity)

    // Add new musicTrack to DB
    // Read all track from DB
    @Query("SELECT * from FavouriteTracks ORDER BY id DESC")
    suspend fun readAllMusicFromDb():List<MusicTrackEntity>
}