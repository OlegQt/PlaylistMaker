package com.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface MusicDao {
    // Add new musicTrack to DB
    @Insert(entity = MusicTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavourite(musTrack:MusicTrackEntity)

    // Read all track from DB
    @Query("SELECT * from FavouriteTracks ORDER BY track_name DESC")
    suspend fun readAllMusicFromDb():List<MusicTrackEntity>

    // Delete all tracks
    @Query("DELETE from FavouriteTracks")
    suspend fun clearFavouriteTracks()

    // Delete single music track
    //@Query("DELETE from FavouriteTracks WHERE id =:trackId")
    //suspend fun deleteTrack(trackId:Long)

    // Функция читает из базы список id из всех треков
    @Query("SELECT id from FavouriteTracks")
    suspend fun getAllTracksId():List<Long>

    @Delete(entity = MusicTrackEntity::class)
    suspend fun deleteFavouriteTrack(musicTrack: MusicTrackEntity)
}