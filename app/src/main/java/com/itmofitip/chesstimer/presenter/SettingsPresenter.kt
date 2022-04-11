package com.itmofitip.chesstimer.presenter

import com.itmofitip.chesstimer.repository.PauseState
import com.itmofitip.chesstimer.repository.SwitchType
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
    private val settingsSwitchesRepository = APP_ACTIVITY.settingsSwitchesRepository
    private val timeRepository = APP_ACTIVITY.timeRepository
    private val pauseRepository = APP_ACTIVITY.pauseRepository

    fun attach() {
        initSwitchesInView()
    }

    fun detach() {
    }

    fun getTimeList(): List<TimeItem> = settingsTimeRepository.getTimeList()

    fun onTimeItemChosen(timeItem: TimeItem) {
        timeRepository.setStartTime(
            TimeUnit.HOURS.toMillis(timeItem.hours.toLong()) +
                    TimeUnit.MINUTES.toMillis(timeItem.minutes.toLong()) +
                    TimeUnit.SECONDS.toMillis(timeItem.seconds.toLong()),
                    TimeUnit.MINUTES.toMillis(timeItem.incrementMinutes.toLong()) +
                    TimeUnit.SECONDS.toMillis(timeItem.incrementSeconds.toLong())
        )
        pauseRepository.setPauseState(PauseState.NOT_STARTED)
        view.onTimeItemChosen()
    }

    fun onSwitchCheckedChange(switchType: SwitchType, isChecked: Boolean) {
        settingsSwitchesRepository.setNewIsChecked(switchType, isChecked)
    }

    fun onDeleteInMenuSelected(position: Int) {
        settingsTimeRepository.removeItem(position)
    }

    private fun initSwitchesInView() {
        with(settingsSwitchesRepository) {
            view.setSwitchIsChecked(SwitchType.SOUND_ON_CLICK, isSoundOnClickChecked)
            view.setSwitchIsChecked(SwitchType.SOUND_ON_LOW_TIME, isSoundOnLowTimeChecked)
            view.setSwitchIsChecked(SwitchType.VIBRATION, isVibrationChecked)
            view.setSwitchIsChecked(SwitchType.DARK_THEME, isDarkThemeChecked)
        }
    }
}