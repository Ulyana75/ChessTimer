package com.itmofitip.chesstimer.view

interface TimerView {
    fun setFirstTime(time: String)
    fun setSecondTime(time: String)

    fun setFirstProgress(progress: Float)
    fun setSecondProgress(progress: Float)

    fun onNotStartedState()
    fun onPauseState()
    fun onActiveState()
}