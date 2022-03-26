package com.itmofitip.chesstimer.presenter

import android.util.Log
import com.itmofitip.chesstimer.repository.*
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class TimerPresenter(private val view: TimerView) {

    private val turnRepository = TurnRepository()
    private val pauseRepository = PauseRepository()
    private var timeRepository = TimeRepository()
    private var firstJob: Job? = null
    private var secondJob: Job? = null

    init {
        view.setFirstTime(getNormalizedTime(timeRepository.startMillis))
        view.setSecondTime(getNormalizedTime(timeRepository.startMillis))

        CoroutineScope(Dispatchers.IO).launch {
            pauseRepository.pauseState.collect {
                when (it) {
                    PauseState.PAUSE -> {
                        firstJob?.cancel()
                        secondJob?.cancel()
                    }
                    PauseState.ACTIVE -> {
                        collectTime()
                    }
                }
            }
        }

        observeMillisLeft()
    }

    fun startTimer() {
        pauseRepository.setPauseState(PauseState.ACTIVE)
    }

    fun stopTimer() {
        pauseRepository.setPauseState(PauseState.PAUSE)
    }

    fun onFirstTimeButtonClicked() {
        startTimer()
        timeRepository.incrementFirstTime()
        turnRepository.setTurn(Turn.SECOND)
    }

    fun onSecondTimeButtonClicked() {
        startTimer()
        timeRepository.incrementSecondTime()
        turnRepository.setTurn(Turn.FIRST)
    }

    @FlowPreview
    private fun observeMillisLeft() {
        CoroutineScope(Dispatchers.IO).launch {
            timeRepository.firstMillisLeft.collect {
                withContext(Dispatchers.Main) {
                    view.setFirstProgress(it.toFloat() / timeRepository.startMillis * 100)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            timeRepository.secondMillisLeft.collect {
                withContext(Dispatchers.Main) {
                    view.setSecondProgress(it.toFloat() / timeRepository.startMillis * 100)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun collectTime() {
        firstJob = CoroutineScope(Dispatchers.Main).launch {
            getFirstTimeFlow().collect { time ->
                view.setFirstTime(time)
            }
        }
        secondJob = CoroutineScope(Dispatchers.Main).launch {
            getSecondTimeFlow().collect { time ->
                view.setSecondTime(time)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getFirstTimeFlow(): Flow<String> {
        return turnRepository.turn.flatMapLatest { turn ->
            when (turn) {
                Turn.FIRST -> return@flatMapLatest flow {
                    while (true) {
                        delay(10)
                        timeRepository.decrementFirstTime(10)
                        emit(getNormalizedTime(timeRepository.firstMillisLeft.value))
                    }
                }.flowOn(Dispatchers.IO)

                else -> return@flatMapLatest flow {
                    emit(getNormalizedTime(timeRepository.firstMillisLeft.value))
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getSecondTimeFlow(): Flow<String> {
        return turnRepository.turn.flatMapLatest { turn ->
            when (turn) {
                Turn.SECOND -> return@flatMapLatest flow {
                    while (true) {
                        delay(10)
                        timeRepository.decrementSecondTime(10)
                        emit(getNormalizedTime(timeRepository.secondMillisLeft.value))
                    }
                }.flowOn(Dispatchers.IO)

                else -> return@flatMapLatest flow {
                    emit(getNormalizedTime(timeRepository.secondMillisLeft.value))
                }
            }
        }
    }

    private fun getNormalizedTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis - minutes * 60_000)
        val strMinutes = if (minutes < 10) "0$minutes" else "$minutes"
        val strSeconds = if (seconds < 10) "0$seconds" else "$seconds"
        return "$strMinutes:$strSeconds"
    }
}