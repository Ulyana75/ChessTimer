package com.itmofitip.chesstimer.repository

import android.content.Context
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class LastChosenTimeRepository : WorkerWithSavedData {

    val defaultLastChosenMillis = TimeUnit.MINUTES.toMillis(3)
    val defaultLastChosenIncrementMillis = TimeUnit.SECONDS.toMillis(2)

    var lastChosenMillis: Long = defaultLastChosenMillis
        private set
    var lastChosenIncrementMillis: Long = defaultLastChosenIncrementMillis
        private set

    override fun initData() {
        lastChosenMillis = APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)
            .getLong(KEY_LAST_CHOSEN_MILLIS, defaultLastChosenMillis)
        lastChosenIncrementMillis = APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)
            .getLong(KEY_LAST_CHOSEN_INCREMENT_MILLIS, defaultLastChosenIncrementMillis)
    }

    override fun saveData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).edit()) {
            putLong(KEY_LAST_CHOSEN_MILLIS, lastChosenMillis)
            putLong(KEY_LAST_CHOSEN_INCREMENT_MILLIS, lastChosenIncrementMillis)
        }.apply()
    }

    fun setNewTime(millis: Long, incrementMillis: Long) {
        lastChosenMillis = millis
        lastChosenIncrementMillis = incrementMillis
    }

    companion object {
        private const val KEY_LAST_CHOSEN_MILLIS = "last_chosen_millis"
        private const val KEY_LAST_CHOSEN_INCREMENT_MILLIS = "last_chosen_increment_millis"
    }
}