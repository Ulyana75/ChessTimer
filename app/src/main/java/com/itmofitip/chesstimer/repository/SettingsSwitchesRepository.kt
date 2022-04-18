package com.itmofitip.chesstimer.repository

import android.content.Context
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsSwitchesRepository : WorkerWithSavedData {
    var isSoundOnClickChecked = true
        private set
    var isSoundOnLowTimeChecked = true
        private set
    var isSoundOnFinishChecked = false
        private set

    private val _isDarkThemeChecked = MutableStateFlow(false)
    val isDarkThemeChecked: StateFlow<Boolean> get() = _isDarkThemeChecked

    override fun initData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)) {
            isSoundOnClickChecked = getBoolean(KEY_TIMER_SOUND, true)
            isSoundOnLowTimeChecked = getBoolean(KEY_LOW_TIME_SOUND, true)
            isSoundOnFinishChecked = getBoolean(KEY_SOUND_ON_FINISH, true)
            _isDarkThemeChecked.value = getBoolean(KEY_DARK_THEME, false)
        }
    }

    override fun saveData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).edit()) {
            putBoolean(KEY_TIMER_SOUND, isSoundOnClickChecked)
            putBoolean(KEY_LOW_TIME_SOUND, isSoundOnLowTimeChecked)
            putBoolean(KEY_SOUND_ON_FINISH, isSoundOnFinishChecked)
            putBoolean(KEY_DARK_THEME, _isDarkThemeChecked.value)
        }.apply()
    }

    fun setNewIsChecked(switchType: SwitchType, isChecked: Boolean) {
        when (switchType) {
            SwitchType.SOUND_ON_CLICK -> isSoundOnClickChecked = isChecked
            SwitchType.SOUND_ON_LOW_TIME -> isSoundOnLowTimeChecked = isChecked
            SwitchType.SOUND_ON_FINISH -> isSoundOnFinishChecked = isChecked
            SwitchType.DARK_THEME -> _isDarkThemeChecked.value = isChecked
        }
    }

    companion object {
        private const val KEY_TIMER_SOUND = "timer_sound"
        private const val KEY_LOW_TIME_SOUND = "low_time_sound"
        private const val KEY_SOUND_ON_FINISH = "sound_on_finish"
        private const val KEY_DARK_THEME = "dark_theme"
    }
}

enum class SwitchType {
    SOUND_ON_CLICK, SOUND_ON_LOW_TIME, SOUND_ON_FINISH, DARK_THEME
}