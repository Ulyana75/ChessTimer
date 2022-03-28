package com.itmofitip.chesstimer.repository

import com.itmofitip.chesstimer.utilities.TimeItem

class SettingsTimeRepository {
    private val defaultTimesList = listOf(
        TimeItem(
            name = "3+2",
            minutes = 3,
            seconds = 0,
            incrementSeconds = 2
        ),
        TimeItem(
            name = "5+5",
            minutes = 5,
            seconds = 0,
            incrementSeconds = 5
        ),
        TimeItem(
            name = "10 min",
            minutes = 10,
            seconds = 0
        ),
        TimeItem(
            name = "15+10",
            minutes = 15,
            seconds = 0,
            incrementSeconds = 10
        ),
        TimeItem(
            name = "1+1",
            minutes = 1,
            seconds = 0,
            incrementSeconds = 1
        )
    )

    fun getTimeList(): List<TimeItem> {
        return defaultTimesList
    }
}