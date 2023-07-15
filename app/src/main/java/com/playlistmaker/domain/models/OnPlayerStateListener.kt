package com.playlistmaker.domain.models

fun interface OnPlayerStateListener {
    fun playerStateChanged(state:PlayerState)
}