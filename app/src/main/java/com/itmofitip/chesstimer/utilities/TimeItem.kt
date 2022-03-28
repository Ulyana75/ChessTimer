package com.itmofitip.chesstimer.utilities

data class TimeItem(
    val name: String,
    val minutes: Long,
    val seconds: Long,
    val incrementMinutes: Long = 0,
    val incrementSeconds: Long = 0
)
