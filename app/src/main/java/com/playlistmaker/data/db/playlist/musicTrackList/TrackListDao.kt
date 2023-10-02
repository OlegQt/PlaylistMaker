package com.playlistmaker.data.db.playlist.musicTrackList

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playlistmaker.data.db.favourite.MusicTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackListDao {
    // Add new musicTrack to TrackList
    @Insert(entity = MusicTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToList(musTrack: MusicTrackEntity)

    // Read all tracks from DB
    @Query("SELECT * from FavouriteTracks")
    fun readAllTrackList(): Flow<List<MusicTrackEntity>>
}