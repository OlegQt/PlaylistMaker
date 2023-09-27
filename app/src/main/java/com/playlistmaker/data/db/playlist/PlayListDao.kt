package com.playlistmaker.data.db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    fun addNewPlayList(playList:PlayListEntity)

}