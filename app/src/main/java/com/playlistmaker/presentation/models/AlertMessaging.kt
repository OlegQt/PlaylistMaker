package com.playlistmaker.presentation.models

interface AlertMessaging {
    fun showAlertDialog(alertMessage: String)
    fun showSnackBar(messageToShow:String)
}