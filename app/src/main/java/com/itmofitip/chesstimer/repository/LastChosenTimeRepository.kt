package com.itmofitip.chesstimer.repository

import android.content.Context
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit

class LastChosenTimeRepository {

    private var lastChosenMillis: Long? = null
    private var lastChosenIncrementMillis: Long? = null

    val defaultLastChosenMillis = TimeUnit.MINUTES.toMillis(3)
    val defaultLastChosenIncrementMillis = TimeUnit.SECONDS.toMillis(2)

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getLastChosenMillis(): Long {
        if (lastChosenMillis == null) {
            lastChosenMillis = APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)
                .getLong(KEY_LAST_CHOSEN_MILLIS, defaultLastChosenMillis)
        }
        return lastChosenMillis!!
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getLastChosenIncrementMillis(): Long {
        if (lastChosenIncrementMillis == null) {
            lastChosenIncrementMillis = APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE)
                .getLong(KEY_LAST_CHOSEN_INCREMENT_MILLIS, defaultLastChosenIncrementMillis)
        }
        return lastChosenIncrementMillis!!
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun setLastChosenTime(millis: Long, incrementMillis: Long) {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).edit()) {
            putLong(KEY_LAST_CHOSEN_MILLIS, millis)
            putLong(KEY_LAST_CHOSEN_INCREMENT_MILLIS, incrementMillis)
        }.apply()
    }

    companion object {
        private const val KEY_LAST_CHOSEN_MILLIS = "last_chosen_millis"
        private const val KEY_LAST_CHOSEN_INCREMENT_MILLIS = "last_chosen_increment_millis"
    }
}