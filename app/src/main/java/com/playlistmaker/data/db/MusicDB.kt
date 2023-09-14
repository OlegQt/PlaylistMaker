package com.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [MusicTrackEntity::class])
abstract  class MusicDB:RoomDatabase() {
    // Возвращает интерфейс для работы с сущностями таблицы FavouriteTracks
    abstract fun musicDao():MusicDao
}