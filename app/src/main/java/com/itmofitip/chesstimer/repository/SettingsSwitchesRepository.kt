package com.itmofitip.chesstimer.repository

import android.content.Context
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsSwitchesRepository : WorkerWithSavedData {
    var isSoundOnClickChecked = true
        private set
    var isSoundOnLowTimeChecked = true
        private set
    var isVibrationChecked = false
        private set
    var isDarkThemeChecked = false
        private set

    override fun initData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)) {
            isSoundOnClickChecked = getBoolean(KEY_TIMER_SOUND, true)
            isSoundOnLowTimeChecked = getBoolean(KEY_LOW_TIME_SOUND, true)
            isVibrationChecked = getBoolean(KEY_VIBRATION, false)
            isDarkThemeChecked = getBoolean(KEY_DARK_THEME, false)
        }
    }

    override fun saveData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).edit()) {
            putBoolean(KEY_TIMER_SOUND, isSoundOnClickChecked)
            putBoolean(KEY_LOW_TIME_SOUND, isSoundOnLowTimeChecked)
            putBoolean(KEY_VIBRATION, isVibrationChecked)
            putBoolean(KEY_DARK_THEME, isDarkThemeChecked)
        }.apply()
    }

    fun setNewIsChecked(switchType: SwitchType, isChecked: Boolean) {
        when (switchType) {
            SwitchType.SOUND_ON_CLICK -> isSoundOnClickChecked = isChecked
            SwitchType.SOUND_ON_LOW_TIME -> isSoundOnLowTimeChecked = isChecked
            SwitchType.VIBRATION -> isVibrationChecked = isChecked
            SwitchType.DARK_THEME -> isDarkThemeChecked = isChecked
        }
    }

    companion object {
        private const val KEY_TIMER_SOUND = "timer_sound"
        private const val KEY_LOW_TIME_SOUND = "low_time_sound"
        private const val KEY_VIBRATION = "vibration"
        private const val KEY_DARK_THEME = "dark_theme"
    }
}

enum class SwitchType {
    SOUND_ON_CLICK, SOUND_ON_LOW_TIME, VIBRATION, DARK_THEME
}