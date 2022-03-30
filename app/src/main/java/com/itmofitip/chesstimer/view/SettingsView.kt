package com.itmofitip.chesstimer.view

import com.itmofitip.chesstimer.repository.SwitchType

interface SettingsView {
    fun onTimeItemChosen()

    fun setSwitchIsChecked(switchType: SwitchType, isChecked: Boolean)
}