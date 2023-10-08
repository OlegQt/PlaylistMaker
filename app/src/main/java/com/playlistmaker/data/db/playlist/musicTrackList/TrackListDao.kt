package com.playlistmaker.data.db.playlist.musicTrackList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playlistmaker.data.db.favourite.MusicTrackEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface TrackListDao {
    // Add new musicTrack to TrackList
    @Insert(entity = AllTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToList(musTrack: MusicTrackEntity)

    // Read all tracks from DB
    @Query("SELECT * from all_tracks_table")
    fun readAllTrackList(): Flow<List<MusicTrackEntity>>

    @Query("DELETE FROM all_tracks_table")
    suspend fun deleteAll()

    @Query("DELETE FROM all_tracks_table WHERE id = :idToDelete")
    fun deleteTrackById(idToDelete:Int)
}