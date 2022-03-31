package com.itmofitip.chesstimer.presenter

import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.TimeItem
import com.itmofitip.chesstimer.utilities.toStringWithTwoDigits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class AddTimePresenter {
    private val settingsTimeRepository = APP_ACTIVITY.settingsTimeRepository

    fun onSaveButtonClicked(
        gameHours: Int,
        gameMinutes: Int,
        gameSeconds: Int,
        incHours: Int,
        incMinutes: Int,
        incSeconds: Int
    ) {
        settingsTimeRepository.addNewTimeItem(
            TimeItem(
                name = getTimeName(
                    gameHours,
                    gameMinutes,
                    gameSeconds,
                    incHours,
                    incMinutes,
                    incSeconds
                ),
                hours = gameHours,
                minutes = gameMinutes,
                seconds = gameSeconds,
                incrementHours = incHours,
                incrementMinutes = incMinutes,
                incrementSeconds = incSeconds
            )
        )
    }

    private fun getTimeName(
        gameHours: Int,
        gameMinutes: Int,
        gameSeconds: Int,
        incHours: Int,
        incMinutes: Int,
        incSeconds: Int
    ): String {
        return when {
            (gameHours == 0 && gameSeconds == 0 && incHours == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameMinutes min"
            (gameHours == 0 && gameSeconds == 0 && incHours == 0 && incMinutes == 0) ->
                "$gameMinutes + $incSeconds"
            (gameHours == 0 && incHours == 0) ->
                "${gameMinutes.toStringWithTwoDigits()}:${gameSeconds.toStringWithTwoDigits()}" +
                        " + ${incMinutes.toStringWithTwoDigits()}:${incSeconds.toStringWithTwoDigits()}"
            else ->
                "${gameHours.toStringWithTwoDigits()}:${gameMinutes.toStringWithTwoDigits()}:${gameSeconds.toStringWithTwoDigits()}" +
                        " + ${incHours.toStringWithTwoDigits()}:${incMinutes.toStringWithTwoDigits()}:${incSeconds.toStringWithTwoDigits()}"
        }
    }
}