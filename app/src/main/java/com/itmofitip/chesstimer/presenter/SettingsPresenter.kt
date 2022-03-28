package com.itmofitip.chesstimer.presenter

import com.itmofitip.chesstimer.repository.PauseState
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.TimeItem
import com.itmofitip.chesstimer.view.SettingsView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsPresenter(private val view: SettingsView) {

    private val settingsTimeRepository = APP_ACTIVITY.settingsTimeRepository
    private val timeRepository = APP_ACTIVITY.timeRepository
    private val pauseRepository = APP_ACTIVITY.pauseRepository

    fun getTimeList(): List<TimeItem> = settingsTimeRepository.getTimeList()

    fun onTimeItemChosen(timeItem: TimeItem) {
        timeRepository.setStartTime(
            TimeUnit.MINUTES.toMillis(timeItem.minutes) + TimeUnit.SECONDS.toMillis(timeItem.seconds),
            TimeUnit.MINUTES.toMillis(timeItem.incrementMinutes) +
                    TimeUnit.SECONDS.toMillis(timeItem.incrementSeconds)
        )
        pauseRepository.setPauseState(PauseState.NOT_STARTED)
        view.onTimeItemChosen()
    }
}