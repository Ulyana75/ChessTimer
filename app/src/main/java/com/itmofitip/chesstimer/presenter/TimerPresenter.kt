package com.itmofitip.chesstimer.presenter

import com.itmofitip.chesstimer.repository.*
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

val FEW_TIME_MILLIS = TimeUnit.MINUTES.toMillis(1)

@FlowPreview
@ExperimentalCoroutinesApi
class TimerPresenter(private val view: TimerView) {

    private val turnRepository = APP_ACTIVITY.turnRepository
    private val pauseRepository = APP_ACTIVITY.pauseRepository
    private val timeRepository = APP_ACTIVITY.timeRepository
    private val timeQuantityRepository = APP_ACTIVITY.timeQuantityRepository
    private var firstJob: Job? = null
    private var secondJob: Job? = null
    private var turnForInactiveJob: Job? = null

    init {
        setStartTime()
        observePauseState()
        observeTimeQuantityState()
        observeMillisLeft()
    }

    private fun setStartTime() {
        view.setFirstTime(getNormalizedTime(timeRepository.startMillis))
        view.setSecondTime(getNormalizedTime(timeRepository.startMillis))
    }

    fun startTimer() {
        if (gameNotFinished()) {
            pauseRepository.setPauseState(PauseState.ACTIVE)
        }
    }

    fun stopTimer() {
        pauseRepository.setPauseState(PauseState.PAUSE)
    }

    fun restartTimer() {
        pauseRepository.setPauseState(PauseState.NOT_STARTED)
    }

    fun onFirstTimeButtonClicked() {
        if (gameNotFinished()) {
            if (pauseRepository.pauseState.value != PauseState.NOT_STARTED && turnRepository.turn.value == Turn.FIRST) {
                timeRepository.incrementFirstTime()
            }
            startTimer()
            turnRepository.setTurn(Turn.SECOND)
        }
    }

    fun onSecondTimeButtonClicked() {
        if (gameNotFinished()) {
            if (pauseRepository.pauseState.value != PauseState.NOT_STARTED && turnRepository.turn.value == Turn.SECOND) {
                timeRepository.incrementSecondTime()
            }
            startTimer()
            turnRepository.setTurn(Turn.FIRST)
        }
    }

    private fun gameNotFinished(): Boolean {
        return (timeQuantityRepository.firstTimeQuantityState.value != TimeQuantityState.FINISHED
                && timeQuantityRepository.secondTimeQuantityState.value != TimeQuantityState.FINISHED)
    }

    private fun cancelAllJobs() {
        firstJob?.cancel()
        secondJob?.cancel()
        turnForInactiveJob?.cancel()
    }

    @FlowPreview
    private fun observeMillisLeft() {
        CoroutineScope(Dispatchers.Main).launch {
            timeRepository.firstMillisLeft.collect {
                view.setFirstProgress(it.toFloat() / timeRepository.startMillis * 100)
                when {
                    it <= 0 -> timeQuantityRepository.setFirstTimeQuantityState(TimeQuantityState.FINISHED)
                    it < FEW_TIME_MILLIS -> timeQuantityRepository.setFirstTimeQuantityState(
                        TimeQuantityState.FEW
                    )
                    else -> timeQuantityRepository.setFirstTimeQuantityState(TimeQuantityState.NORMAL)
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            timeRepository.secondMillisLeft.collect {
                view.setSecondProgress(it.toFloat() / timeRepository.startMillis * 100)
                when {
                    it <= 0 -> timeQuantityRepository.setSecondTimeQuantityState(TimeQuantityState.FINISHED)
                    it < FEW_TIME_MILLIS -> timeQuantityRepository.setSecondTimeQuantityState(
                        TimeQuantityState.FEW
                    )
                    else -> timeQuantityRepository.setSecondTimeQuantityState(TimeQuantityState.NORMAL)
                }
            }
        }
    }

    private fun observeTimeQuantityState() {
        CoroutineScope(Dispatchers.Main).launch {
            timeQuantityRepository.firstTimeQuantityState.collect {
                when (it) {
                    TimeQuantityState.FEW -> view.onFirstFewTime()
                    TimeQuantityState.FINISHED -> {
                        view.onFirstFinished()
                        stopTimer()
                    }
                    TimeQuantityState.NORMAL -> view.onFirstNormal()
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            timeQuantityRepository.secondTimeQuantityState.collect {
                when (it) {
                    TimeQuantityState.FEW -> view.onSecondFewTime()
                    TimeQuantityState.FINISHED -> {
                        view.onSecondFinished()
                        stopTimer()
                    }
                    TimeQuantityState.NORMAL -> view.onSecondNormal()
                }
            }
        }
    }

    private fun observeTurnForInactive() {
        turnForInactiveJob = CoroutineScope(Dispatchers.Main).launch {
            turnRepository.turn.collect {
                when (it) {
                    Turn.FIRST -> view.setSecondInactive()
                    else -> view.setFirstInactive()
                }
            }
        }
    }

    private fun observePauseState() {
        CoroutineScope(Dispatchers.IO).launch {
            pauseRepository.pauseState.collect {
                when (it) {
                    PauseState.PAUSE -> {
                        withContext(Dispatchers.Main) {
                            view.onPauseState()
                        }
                        cancelAllJobs()
                    }
                    PauseState.ACTIVE -> {
                        withContext(Dispatchers.Main) {
                            view.onActiveState()
                        }
                        observeTurnForInactive()
                        observeTime()
                    }
                    PauseState.NOT_STARTED -> {
                        timeRepository.resetTime()
                        cancelAllJobs()
                        withContext(Dispatchers.Main) {
                            view.onNotStartedState()
                            setStartTime()
                        }
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun observeTime() {
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