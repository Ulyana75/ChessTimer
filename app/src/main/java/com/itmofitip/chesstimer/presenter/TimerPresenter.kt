package com.itmofitip.chesstimer.presenter

import android.util.Log
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TimerPresenter(private val view: TimerView) {

    private val turn = MutableStateFlow<Int?>(null)
    private val isPause = MutableStateFlow(true)
    private val startFirstTime = "1"
    private val startSecondTime = "1"
    var currentFirstTime = startFirstTime
    var currentSecondTime = startSecondTime
    private var activeJob: Job? = null

    init {
        view.setFirstTime(startFirstTime)
        view.setSecondTime(startSecondTime)

        CoroutineScope(Dispatchers.IO).launch {
            isPause.collect {
                if (it) {
                    activeJob?.cancel()
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            observeTurn()
        }
    }

    fun startTimer() {
        isPause.value = false
        CoroutineScope(Dispatchers.IO).launch {
            observeTurn()
        }
    }

    fun stopTimer() {
        isPause.value = true
    }

    fun onFirstTimeButtonClicked() {
        Log.d("LOL", "onFirstTimeButtonClicked")
        turn.value = 1
    }

    fun onSecondTimeButtonClicked() {
        Log.d("LOL", "onSecondTimeButtonClicked")
        turn.value = 0
    }

    private suspend fun observeTurn() {
        Log.d("LOL", "observeTurn")
        turn.collect {
            Log.d("LOL", "im in collect turn with value $it")
            activeJob?.cancel()

            when (it) {
                0 -> {
                    activeJob = getTimerJob(0)
                }
                1 -> {
                    activeJob = getTimerJob(1)
                }
            }
        }
    }

    private fun getTimerJob(a: Int): Job {
        return when (a) {
            0 -> CoroutineScope(Dispatchers.IO).launch {
                getFirstTimeFlow().collect { time ->
                    withContext(Dispatchers.Main) {
                        view.setFirstTime(time)
                    }
                }
            }
            else -> CoroutineScope(Dispatchers.IO).launch {
                getSecondTimeFlow().collect { time ->
                    withContext(Dispatchers.Main) {
                        view.setSecondTime(time)
                    }
                }
            }
        }
    }

    private fun getFirstTimeFlow(): Flow<String> {
        return flow {
            while (true) {
                delay(1000)
                currentFirstTime += '1'
                emit(currentFirstTime)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getSecondTimeFlow(): Flow<String> {
        return flow {
            while (true) {
                delay(1000)
                currentSecondTime += '1'
                emit(currentSecondTime)
            }
        }.flowOn(Dispatchers.IO)
    }
}