package com.playlistmaker.data.db.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayListDB")
data class PlayListEntity(
    @PrimaryKey (autoGenerate = true)
    val id:Long,
    @ColumnInfo(name = "playlist_name")
    val name:String,
    @ColumnInfo(name = "playlist_description")
    val description:String,
    @ColumnInfo(name = "playlist_cover_path")
    val cover:String,
    @ColumnInfo(name = "playlist_tracks")
    val trackList:String,
    @ColumnInfo(name = "playlist_track_quantity")
    val quantity:Int
)
