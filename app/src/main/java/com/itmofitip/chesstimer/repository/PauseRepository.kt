package com.itmofitip.chesstimer.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PauseRepository {
    private val _pauseState: MutableStateFlow<PauseState> = MutableStateFlow(PauseState.NOT_STARTED)
    val pauseState: StateFlow<PauseState> get() = _pauseState

    fun setPauseState(newPauseState: PauseState) {
        _pauseState.value = newPauseState
    }
}

enum class PauseState {
    PAUSE, ACTIVE, NOT_STARTED
}