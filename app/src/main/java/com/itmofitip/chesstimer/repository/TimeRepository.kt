package com.itmofitip.chesstimer.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class TimeRepository {
    var startMillis: Long = TimeUnit.SECONDS.toMillis(10)
        private set
    var increment: Long = TimeUnit.SECONDS.toMillis(2)
        private set

    private val _firstMillisLeft = MutableStateFlow(startMillis)
    val firstMillisLeft: StateFlow<Long> get() = _firstMillisLeft

    private val _secondMillisLeft = MutableStateFlow(startMillis)
    val secondMillisLeft: StateFlow<Long> get() = _secondMillisLeft


    fun incrementFirstTime() {
        _firstMillisLeft.value += increment
    }

    fun incrementSecondTime() {
        _secondMillisLeft.value += increment
    }

    fun decrementFirstTime(decrement: Long) {
        _firstMillisLeft.value -= decrement
    }

    fun decrementSecondTime(decrement: Long) {
        _secondMillisLeft.value -= decrement
    }

    fun resetTime() {
        _firstMillisLeft.value = startMillis
        _secondMillisLeft.value = startMillis
    }

    fun setStartTime(newStartMillis: Long, newIncrement: Long) {
        startMillis = newStartMillis
        increment = newIncrement
    }
}