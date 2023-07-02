package com.playlistmaker.data.mapper

import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.domain.models.MusicTrack

class MusicTrackMapper {
    fun mapToDto(musicTrack: MusicTrack): MusicTrackDto {
        return MusicTrackDto(
            trackName =musicTrack.trackName ,
            artistName=musicTrack.artistName,
            trackTimeMillis = musicTrack.trackTimeMillis,
            artworkUrl100 = musicTrack.artworkUrl100,
            trackId =musicTrack.trackId,
            collectionName = musicTrack.collectionName,
            releaseDate = musicTrack.releaseDate,
            primaryGenreName = musicTrack.primaryGenreName,
            country = musicTrack.country,
            previewUrl = musicTrack.previewUrl
        )
    }

    fun mapFromDto(musicTrackDto: MusicTrackDto): MusicTrack {
        return MusicTrack(
            trackName =musicTrackDto.trackName ,
            artistName=musicTrackDto.artistName,
            trackTimeMillis = musicTrackDto.trackTimeMillis,
            artworkUrl100 = musicTrackDto.artworkUrl100,
            trackId =musicTrackDto.trackId,
            collectionName = musicTrackDto.collectionName,
            releaseDate = musicTrackDto.releaseDate,
            primaryGenreName = musicTrackDto.primaryGenreName,
            country = musicTrackDto.country,
            previewUrl = musicTrackDto.previewUrl
        )
    }
}