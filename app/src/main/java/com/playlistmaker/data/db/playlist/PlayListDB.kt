package com.playlistmaker.data.db.playlist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [PlayListEntity::class])
abstract class PlayListDB :RoomDatabase(){
    abstract fun playListDao(): PlayListDao
}