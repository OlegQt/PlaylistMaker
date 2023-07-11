package com.playlistmaker.data.repository

import android.app.Application
import android.content.Context
import com.playlistmaker.Theme.App
import com.playlistmaker.domain.repository.ScreenRepository

private const val PREFERENCES ="APP_PREFERENCES"
private const val CURRENT_SCREEN = "key_for_saving_current_string"
private const val DEFAULT_SCREEN ="main_screen"

class ScreenRepositoryImpl(context:Context): ScreenRepository {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES, Application.MODE_PRIVATE)

    override fun showScreen(screenName: String) {
        //TODO("Not yet implemented")
    }

    override fun safeLastScreen(screenName: String) {
        sharedPreferences.edit().putString(CURRENT_SCREEN, screenName).apply()
    }

    override fun loadLastScreen(): String {
        return sharedPreferences.getString(App.CURRENT_SCREEN, "").toString()
    }
}