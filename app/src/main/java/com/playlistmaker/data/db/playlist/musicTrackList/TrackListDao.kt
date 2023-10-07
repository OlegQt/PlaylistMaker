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
    @Insert(entity = AllTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToList(musTrack: MusicTrackEntity)

    // Read all tracks from DB
    @Query("SELECT * from all_tracks_table")
    fun readAllTrackList(): Flow<List<MusicTrackEntity>>
}