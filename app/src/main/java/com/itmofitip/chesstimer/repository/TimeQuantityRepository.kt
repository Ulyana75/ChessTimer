package com.itmofitip.chesstimer.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimeQuantityRepository {
    private val _firstTimeQuantityState: MutableStateFlow<TimeQuantityState> =
        MutableStateFlow(TimeQuantityState.NORMAL)
    val firstTimeQuantityState: StateFlow<TimeQuantityState> get() = _firstTimeQuantityState

    private val _secondTimeQuantityState: MutableStateFlow<TimeQuantityState> =
        MutableStateFlow(TimeQuantityState.NORMAL)
    val secondTimeQuantityState: StateFlow<TimeQuantityState> get() = _secondTimeQuantityState


    fun setFirstTimeQuantityState(newState: TimeQuantityState) {
        _firstTimeQuantityState.value = newState
    }

    fun setSecondTimeQuantityState(newState: TimeQuantityState) {
        _secondTimeQuantityState.value = newState
    }
}

enum class TimeQuantityState {
    FEW, FINISHED, NORMAL, NEED_MS
}