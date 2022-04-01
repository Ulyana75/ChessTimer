package com.itmofitip.chesstimer.repository

import android.content.Context
import com.google.gson.Gson
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.TimeItem
import com.itmofitip.chesstimer.utilities.TimeItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsTimeRepository : WorkerWithSavedData {
    private val defaultTimeItems = TimeItems(
        mutableListOf(
            TimeItem(
                name = "3 + 2",
                minutes = 3,
                seconds = 0,
                incrementSeconds = 2
            ),
            TimeItem(
                name = "5 + 5",
                minutes = 5,
                seconds = 0,
                incrementSeconds = 5
            ),
            TimeItem(
                name = "10 min",
                minutes = 10,
                seconds = 0
            ),
            TimeItem(
                name = "15 + 10",
                minutes = 15,
                seconds = 0,
                incrementSeconds = 10
            ),
            TimeItem(
                name = "1 + 1",
                minutes = 1,
                seconds = 0,
                incrementSeconds = 1
            )
        )
    )

    private val gson = Gson()
    private var timeItems = defaultTimeItems

    fun getTimeList(): List<TimeItem> {
        return timeItems.items
    }

    override fun initData() {
        val timeItemsJson =
            APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).getString(KEY_TIME_ITEMS, null)
        if (timeItemsJson != null) {
            timeItems = gson.fromJson(timeItemsJson, TimeItems::class.java)
        }
    }

    override fun saveData() {
        with(APP_ACTIVITY.getPreferences(Context.MODE_PRIVATE).edit()) {
            putString(KEY_TIME_ITEMS, gson.toJson(timeItems))
        }.apply()
    }

    fun addNewTimeItem(item: TimeItem) {
        timeItems.items.add(0, item)
    }

    companion object {
        const val KEY_TIME_ITEMS = "time_items"
    }
}