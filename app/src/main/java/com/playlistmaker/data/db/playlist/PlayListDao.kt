package com.playlistmaker.data.db.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {
    // Save new playList to DB
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewPlayList(playList: PlayListEntity)

    // Read all DB content
    @Query("SELECT * from PlayListDB")
    fun getAllPlayLists(): Flow<List<PlayListEntity?>>

    @Query("SELECT * FROM PlayListDB WHERE id = :playlistId")
    fun getPlayListById(playlistId: Long): Flow<PlayListEntity>


    @Delete
    suspend fun deletePlaylist(playlist: PlayListEntity)

    @Query("DELETE FROM PlayListDB")
    suspend fun deleteAllPlaylists()

    @Update
    suspend fun updatePlaylist(playlist: PlayListEntity)

}