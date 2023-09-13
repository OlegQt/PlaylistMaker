package com.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteTracks")
data class MusicTrackEntity(
        @PrimaryKey
        val id:Int
)
