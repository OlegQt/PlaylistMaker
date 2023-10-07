package com.playlistmaker.data.db.playlist.musicTrackList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_tracks_table")
data class AllTracksEntity(
        @PrimaryKey
        val id:Long,
        @ColumnInfo(name = "track_name")
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Long,
        val artworkUrl100: String,
        val collectionName: String,
        val releaseDate: String,
        val primaryGenreName: String,
        val country: String,
        val previewUrl: String,
        @ColumnInfo(name = "insert_time")
        val timeInset:Long
)