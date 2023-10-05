package com.playlistmaker.data.db.favourite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.playlistmaker.data.db.playlist.PlayListDao
import com.playlistmaker.data.db.playlist.PlayListEntity
import com.playlistmaker.data.db.playlist.musicTrackList.AllTracksEntity
import com.playlistmaker.data.db.playlist.musicTrackList.TrackListDao

@Database(version = 1, entities = [MusicTrackEntity::class, PlayListEntity::class,AllTracksEntity::class])
abstract class MusicDB : RoomDatabase() {
    // Возвращает интерфейс для работы с сущностями таблицы FavouriteTracks
    abstract fun musicDao(): MusicDao
    abstract fun playListDao(): PlayListDao
    abstract fun trackListDao(): TrackListDao

}