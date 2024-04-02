package com.playlistmaker.presentation.ui.musicservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicPlayerService:Service() {

    // Binder given to clients.
    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {

        return binder
    }

    override fun onCreate() {
        super.onCreate()

        Log.e("LOG","Service created")
    }

    private fun initialisePlayer(){

    }

    private fun startPlayingMusic(){

    }

    private fun stopPlayingMusic(){

    }

    private fun releasePlayerResources(){

    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }


}