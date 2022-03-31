package com.itmofitip.chesstimer.utilities

data class TimeItems(
    val items: MutableList<TimeItem>
)

data class TimeItem(
    val name: String,
    val hours: Int = 0,
    val minutes: Int,
    val seconds: Int,
    val incrementHours: Int = 0,
    val incrementMinutes: Int = 0,
    val incrementSeconds: Int = 0
)
