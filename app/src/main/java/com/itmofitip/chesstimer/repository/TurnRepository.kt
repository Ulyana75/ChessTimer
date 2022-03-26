package com.itmofitip.chesstimer.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TurnRepository {
    private val _turn: MutableStateFlow<Turn> = MutableStateFlow(Turn.FIRST)
    val turn: StateFlow<Turn> get() = _turn

    fun setTurn(newTurn: Turn) {
        _turn.value = newTurn
    }
}

enum class Turn {
    FIRST, SECOND
}