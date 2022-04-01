package com.itmofitip.chesstimer.view

interface TimerView {
    fun setFirstTime(time: String)
    fun setSecondTime(time: String)

    fun setFirstProgress(progress: Float)
    fun setSecondProgress(progress: Float)

    fun setFirstInactive()
    fun setSecondInactive()

    fun onNotStartedState()
    fun onPauseState()
    fun onActiveState()

    fun onFirstFewTime()
    fun onSecondFewTime()
    fun onFirstFinished()
    fun onSecondFinished()
    fun onFirstNormal()
    fun onSecondNormal()

    fun onFirstShortTimeStr()
    fun onSecondShortTimeStr()
    fun onFirstLongTimeStr()
    fun onSecondLongTimeStr()
}