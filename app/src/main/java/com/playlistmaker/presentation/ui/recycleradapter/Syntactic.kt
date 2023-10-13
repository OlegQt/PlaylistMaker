package com.playlistmaker.presentation.ui.recycleradapter

object Syntactic {
    fun getTrackEnding(number: Int): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number трек"
            number % 10 in (2..4) && (number % 100 < 10) -> "$number трека"
            number % 10 in (2..4) && (number % 100 >20) -> "$number трека"
            else -> "$number треков"
        }
    }

    fun getMinuteEnding(number: Int): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number минута"
            number % 10 in (2..4) && (number % 100 < 10) -> "$number минуты"
            number % 10 in (2..4) && (number % 100 >20) -> "$number минуты"
            else -> "$number минут"
        }
    }
}