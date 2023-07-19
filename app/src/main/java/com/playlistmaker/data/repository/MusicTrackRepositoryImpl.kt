package com.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.playlistmaker.data.dto.MusicTrackDto
import com.playlistmaker.data.mapper.MusicTrackMapper
import com.playlistmaker.domain.models.ErrorList
import com.playlistmaker.domain.models.MusicTrack
import com.playlistmaker.domain.repository.MusicTrackRepository
import com.playlistmaker.util.Resource
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin

private const val CURRENT_PLAYING_TRACK = "key_for_saving_current_track"

class MusicTrackRepositoryImpl(context: Context): MusicTrackRepository{
    private val sharedPreferences:SharedPreferences = getKoin().get() { parametersOf(context)}
    private val gSon: Gson = KoinJavaComponent.getKoin().get()

    override fun getCurrentMusicTrack(): Resource<MusicTrack> {
        // Загрузили трек
        val jsonTrack = sharedPreferences.getString(CURRENT_PLAYING_TRACK, "")

        // Если трек загружен успешно форматируем в класс DTO и делаем mapping
        return if(jsonTrack.isNullOrEmpty()) Resource.Error(ErrorList.UNKNOWN_ERROR)
        else {
            val jTrack = gSon.fromJson(jsonTrack, MusicTrackDto::class.java)
            val modelTrack = MusicTrackMapper().mapFromDto(jTrack)
            return Resource.Success(modelTrack)
        }
    }

    override fun setCurrentMusicTrack(musicTrack: MusicTrack) {
        // Делаем mapping
        val musicTrackDto = MusicTrackMapper().mapToDto(musicTrack)

        // Сохраняем трек
        val jsonTrack = gSon.toJson(musicTrackDto, MusicTrackDto::class.java)
        sharedPreferences.edit().putString(CURRENT_PLAYING_TRACK, jsonTrack).apply()
    }
}