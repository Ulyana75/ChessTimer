package com.itmofitip.chesstimer.repository

import com.itmofitip.chesstimer.commonmodels.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TimerRepository {
    private var startTime: Time = Time(3)
    private var currentFirstTime = startTime.copy()
    private var currentSecondTime = startTime.copy()

    fun getStartTime(): String = "${startTime.minutes}:${startTime.seconds}"

    fun getFirstTimeFlow(): Flow<String> {
        return flow {
            while (true) {
                delay(10)
                currentFirstTime.subtractMillis(10)
                emit("${currentFirstTime.minutes}:${currentFirstTime.seconds}")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSecondTimeFlow(): Flow<String> {
        return flow {
            while (true) {
                delay(10)
                currentSecondTime.subtractMillis(10)
                emit("${currentSecondTime.minutes}:${currentSecondTime.seconds}")
            }
        }.flowOn(Dispatchers.IO)
    }
}