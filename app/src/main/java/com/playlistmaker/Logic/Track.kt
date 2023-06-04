package com.playlistmaker.Logic


data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId:Long,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country:String
)