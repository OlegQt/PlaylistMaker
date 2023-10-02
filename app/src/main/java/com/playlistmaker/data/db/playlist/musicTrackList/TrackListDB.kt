package com.playlistmaker.data.db.playlist.musicTrackList

import androidx.room.Database
import androidx.room.RoomDatabase
import com.playlistmaker.data.db.favourite.MusicTrackEntity

@Database(version = 1, entities = [MusicTrackEntity::class])
abstract class TrackListDB : RoomDatabase() {
    abstract fun trackListDao(): TrackListDao
}
