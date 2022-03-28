package com.itmofitip.chesstimer.utilities

import java.io.Serializable

data class TimeItem(
    val name: String,
    val minutes: Long,
    val seconds: Long,
    val incrementMinutes: Long = 0,
    val incrementSeconds: Long = 0
) : Serializable
