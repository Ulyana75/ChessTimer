package com.itmofitip.chesstimer.presenter

import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.TimeItem
import com.itmofitip.chesstimer.utilities.toStringWithTwoDigits
import com.itmofitip.chesstimer.view.AddTimeView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class AddTimePresenter(private val view: AddTimeView) {
    private val settingsTimeRepository = APP_ACTIVITY.settingsTimeRepository

    fun onSaveButtonClicked(
        gameHours: Int,
        gameMinutes: Int,
        gameSeconds: Int,
        incMinutes: Int,
        incSeconds: Int
    ) {
        if (gameHours == 0 && gameMinutes == 0 && gameSeconds == 0 && incMinutes == 0 && incSeconds == 0) {
            view.onWrongEnteredTime()
            return
        }
        settingsTimeRepository.addNewTimeItem(
            TimeItem(
                name = getTimeName(
                    gameHours,
                    gameMinutes,
                    gameSeconds,
                    incMinutes,
                    incSeconds
                ),
                hours = gameHours,
                minutes = gameMinutes,
                seconds = gameSeconds,
                incrementMinutes = incMinutes,
                incrementSeconds = incSeconds
            )
        )
        view.onSaveButtonClicked()
    }

    private fun getTimeName(
        gameHours: Int,
        gameMinutes: Int,
        gameSeconds: Int,
        incMinutes: Int,
        incSeconds: Int
    ): String {
        return when {
            (gameMinutes == 0 && gameSeconds == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameHours h"
            (gameHours == 0 && gameSeconds == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameMinutes min"
            (gameHours == 0 && gameMinutes == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameSeconds s"
            (gameSeconds == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameHours h $gameMinutes m"
            (gameHours == 0 && incMinutes == 0 && incSeconds == 0) ->
                "$gameMinutes m $gameSeconds s"
            (incMinutes == 0 && incSeconds == 0) ->
                "$gameHours h $gameMinutes m $gameSeconds s"

            (gameHours == 0 && gameSeconds == 0 && incMinutes == 0) ->
                "$gameMinutes + $incSeconds"
            (gameHours == 0) ->
                "${gameMinutes.toStringWithTwoDigits()}:${gameSeconds.toStringWithTwoDigits()}" +
                        " + ${incMinutes.toStringWithTwoDigits()}:${incSeconds.toStringWithTwoDigits()}"
            else ->
                "${gameHours.toStringWithTwoDigits()}:${gameMinutes.toStringWithTwoDigits()}:${gameSeconds.toStringWithTwoDigits()}" +
                        " + ${incMinutes.toStringWithTwoDigits()}:${incSeconds.toStringWithTwoDigits()}"
        }
    }
}