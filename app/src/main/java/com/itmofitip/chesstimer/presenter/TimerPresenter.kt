package com.itmofitip.chesstimer.presenter

import android.media.MediaPlayer
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.repository.PauseState
import com.itmofitip.chesstimer.repository.TimeQuantityState
import com.itmofitip.chesstimer.repository.Turn
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.getNormalizedTime
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

val FEW_TIME_MILLIS = TimeUnit.SECONDS.toMillis(30)
val NEED_MS_TIME_MILLIS = TimeUnit.SECONDS.toMillis(20)
const val LONG_TIME_MINIMAL_LENGTH = 8

@FlowPreview
@ExperimentalCoroutinesApi
class TimerPresenter(private val view: TimerView) {

    private val turnRepository = APP_ACTIVITY.turnRepository
    private val pauseRepository = APP_ACTIVITY.pauseRepository
    private val timeRepository = APP_ACTIVITY.timeRepository
    private val timeQuantityRepository = APP_ACTIVITY.timeQuantityRepository
    private val settingsSwitchesRepository = APP_ACTIVITY.settingsSwitchesRepository
    private var firstJob: Job? = null
    private var secondJob: Job? = null
    private var turnForInactiveJob: Job? = null

    private val activeJobs = mutableListOf<Job>()


    fun attach() {
        setTime()
        observePauseState()
        observeTimeQuantityState()
        observeMillisLeft()
    }

    fun detach() {
        if (pauseRepository.pauseState.value == PauseState.ACTIVE) {
            stopTimer()
        }
        cancelAllJobs()
    }

    private fun setTime() {
        adjustTextSize(getNormalizedTime(timeRepository.firstMillisLeft.value))
        view.setFirstTime(getNormalizedTime(timeRepository.firstMillisLeft.value))
        view.setSecondTime(getNormalizedTime(timeRepository.secondMillisLeft.value))
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
            if (turnRepository.turn.value == Turn.FIRST) {
                makeSound()
                if (pauseRepository.pauseState.value != PauseState.NOT_STARTED) {
                    timeRepository.incrementFirstTime()
                }
            }
            startTimer()
            turnRepository.setTurn(Turn.SECOND)
        }
    }

    fun onSecondTimeButtonClicked() {
        if (gameNotFinished()) {
            if (turnRepository.turn.value == Turn.SECOND) {
                makeSound()
                if (pauseRepository.pauseState.value != PauseState.NOT_STARTED) {
                    timeRepository.incrementSecondTime()
                }
            }
            startTimer()
            turnRepository.setTurn(Turn.FIRST)
        }
    }

    private fun makeSound() {
        if (settingsSwitchesRepository.isSoundOnClickChecked) {
            val mp = MediaPlayer.create(APP_ACTIVITY, R.raw.timer_tap)
            mp.setOnCompletionListener {
                mp.release()
            }
            mp.start()
        }
    }

    private fun gameNotFinished(): Boolean {
        return (timeQuantityRepository.firstTimeQuantityState.value != TimeQuantityState.FINISHED
                && timeQuantityRepository.secondTimeQuantityState.value != TimeQuantityState.FINISHED)
    }

    private fun cancelAllJobs() {
        activeJobs.forEach { it.cancel() }
        activeJobs.clear()
        firstJob?.cancel()
        secondJob?.cancel()
        turnForInactiveJob?.cancel()
    }

    @FlowPreview
    private fun observeMillisLeft() {
        activeJobs.add(CoroutineScope(Dispatchers.Main).launch {
            timeRepository.firstMillisLeft.collect {
                view.setFirstProgress(it.toFloat() / timeRepository.startMillis * 100)
                when {
                    it <= 0 -> timeQuantityRepository.setFirstTimeQuantityState(TimeQuantityState.FINISHED)
                    it < NEED_MS_TIME_MILLIS -> {
                        timeQuantityRepository.setFirstTimeQuantityState(TimeQuantityState.NEED_MS)
                        view.setFirstMs("${it % 1000}ms")
                    }
                    it < FEW_TIME_MILLIS -> timeQuantityRepository.setFirstTimeQuantityState(
                        TimeQuantityState.FEW
                    )
                    else -> timeQuantityRepository.setFirstTimeQuantityState(TimeQuantityState.NORMAL)
                }
            }
        })
        activeJobs.add(CoroutineScope(Dispatchers.Main).launch {
            timeRepository.secondMillisLeft.collect {
                view.setSecondProgress(it.toFloat() / timeRepository.startMillis * 100)
                when {
                    it <= 0 -> timeQuantityRepository.setSecondTimeQuantityState(TimeQuantityState.FINISHED)
                    it < NEED_MS_TIME_MILLIS -> {
                        timeQuantityRepository.setSecondTimeQuantityState(TimeQuantityState.NEED_MS)
                        view.setSecondMs("${it % 1000}ms")
                    }
                    it < FEW_TIME_MILLIS -> timeQuantityRepository.setSecondTimeQuantityState(
                        TimeQuantityState.FEW
                    )
                    else -> timeQuantityRepository.setSecondTimeQuantityState(TimeQuantityState.NORMAL)
                }
            }
        })
    }

    private fun observeTimeQuantityState() {
        activeJobs.add(CoroutineScope(Dispatchers.Main).launch {
            timeQuantityRepository.firstTimeQuantityState.collect {
                when (it) {
                    TimeQuantityState.FEW -> view.onFirstFewTime()
                    TimeQuantityState.FINISHED -> {
                        view.onFirstFinished()
                        stopTimer()
                    }
                    TimeQuantityState.NORMAL -> view.onFirstNormal()
                    TimeQuantityState.NEED_MS -> view.onFirstNeedMs()
                }
            }
        })
        activeJobs.add(CoroutineScope(Dispatchers.Main).launch {
            timeQuantityRepository.secondTimeQuantityState.collect {
                when (it) {
                    TimeQuantityState.FEW -> view.onSecondFewTime()
                    TimeQuantityState.FINISHED -> {
                        view.onSecondFinished()
                        stopTimer()
                    }
                    TimeQuantityState.NORMAL -> view.onSecondNormal()
                    TimeQuantityState.NEED_MS -> view.onSecondNeedMs()
                }
            }
        })
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
        activeJobs.add(CoroutineScope(Dispatchers.IO).launch {
            pauseRepository.pauseState.collect {
                when (it) {
                    PauseState.PAUSE -> {
                        withContext(Dispatchers.Main) {
                            view.onPauseState()
                        }
                        firstJob?.cancel()
                        secondJob?.cancel()
                        turnForInactiveJob?.cancel()
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
                        firstJob?.cancel()
                        secondJob?.cancel()
                        turnForInactiveJob?.cancel()
                        withContext(Dispatchers.Main) {
                            view.onNotStartedState()
                            setTime()
                        }
                    }
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    private fun observeTime() {
        firstJob = CoroutineScope(Dispatchers.Main).launch {
            getFirstTimeFlow().collect { time ->
                if (time.length >= LONG_TIME_MINIMAL_LENGTH) view.onFirstLongTimeStr()
                else view.onFirstShortTimeStr()
                view.setFirstTime(time)
            }
        }
        secondJob = CoroutineScope(Dispatchers.Main).launch {
            getSecondTimeFlow().collect { time ->
                if (time.length >= LONG_TIME_MINIMAL_LENGTH) view.onSecondLongTimeStr()
                else view.onSecondShortTimeStr()
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

    private fun adjustTextSize(str: String) {
        if (str.length >= LONG_TIME_MINIMAL_LENGTH) {
            view.onFirstLongTimeStr()
            view.onSecondLongTimeStr()
        } else {
            view.onFirstShortTimeStr()
            view.onSecondShortTimeStr()
        }
    }
}