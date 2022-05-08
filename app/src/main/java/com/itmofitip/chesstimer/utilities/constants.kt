package com.itmofitip.chesstimer.utilities

import com.itmofitip.chesstimer.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
lateinit var APP_ACTIVITY: MainActivity

const val ANIMATION_DURATION = 200L

val FEW_TIME_MILLIS = TimeUnit.SECONDS.toMillis(30)
val NEED_MS_TIME_MILLIS = TimeUnit.SECONDS.toMillis(20)
val SOUND_LOW_TIME_MILLIS = TimeUnit.SECONDS.toMillis(15)
const val LONG_TIME_MINIMAL_LENGTH = 8
const val MILLIS_DELAY = 20L