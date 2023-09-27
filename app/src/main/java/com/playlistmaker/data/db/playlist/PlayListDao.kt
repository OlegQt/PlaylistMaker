package com.playlistmaker.data.db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playlistmaker.data.db.favourite.MusicTrackEntity

@Dao
interface PlayListDao {
    // Save new playList to DB
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewPlayList(playList: PlayListEntity)

    // Read all DB content
    @Query("SELECT * from PlayListDB ORDER BY playlist_name DESC")
    suspend fun getAllPlayLists(): List<PlayListEntity>

}