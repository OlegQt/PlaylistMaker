package com.playlistmaker.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.playlistmaker.appstart.App

class ActivitySettingsVm() : ViewModel() {
    var theme: Int = App.instance.getCurrentTheme()

    fun switchTheme() {
        when (theme) {
            1 -> theme = 0
            0 -> theme = 1
        }
        App.instance.applyTheme(theme)
    }


}