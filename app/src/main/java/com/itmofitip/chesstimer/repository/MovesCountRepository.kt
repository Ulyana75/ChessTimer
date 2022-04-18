package com.itmofitip.chesstimer.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovesCountRepository {
    private val _movesCountFirst = MutableStateFlow(0)
    val movesCountFirst: StateFlow<Int> get() = _movesCountFirst

    private val _movesCountSecond = MutableStateFlow(0)
    val movesCountSecond: StateFlow<Int> get() = _movesCountSecond

    fun incrementFirst() {
        _movesCountFirst.value++
    }

    fun incrementSecond() {
        _movesCountSecond.value++
    }

    fun setMovesCounts(valueFirst: Int, valueSecond: Int) {
        _movesCountFirst.value = valueFirst
        _movesCountSecond.value = valueSecond
    }
}