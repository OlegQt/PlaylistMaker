package com.playlistmaker.presentation.models

interface SearchActivityView {
    fun showAlertDialog(msg:String)
    fun render(state: ActivitySearchState)
    fun startPlayerActivity()
}