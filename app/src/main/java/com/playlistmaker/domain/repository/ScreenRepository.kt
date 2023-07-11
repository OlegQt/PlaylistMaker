package com.playlistmaker.domain.repository

interface ScreenRepository {
    fun showScreen(screenName:String)
    fun safeLastScreen(screenName: String)
    fun loadLastScreen():String
}