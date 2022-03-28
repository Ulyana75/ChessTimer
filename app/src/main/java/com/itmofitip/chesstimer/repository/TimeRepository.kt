package com.itmofitip.chesstimer.repository

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class TimeRepository {
    private val lastChosenTimeRepository = LastChosenTimeRepository()

    var startMillis: Long = lastChosenTimeRepository.defaultLastChosenMillis
        private set
    var increment: Long = lastChosenTimeRepository.defaultLastChosenIncrementMillis
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

    fun initStartTime() {
        setStartTime(
            lastChosenTimeRepository.getLastChosenMillis(),
            lastChosenTimeRepository.getLastChosenIncrementMillis()
        )
    }

    fun setLastChosenTime() {
        lastChosenTimeRepository.setLastChosenTime(startMillis, increment)
    }
}