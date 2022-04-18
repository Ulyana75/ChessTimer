package com.itmofitip.chesstimer.repository

class WasSoundLowTimeRepository {
    var wasFirstSound = false
    var wasSecondSound = false

    fun setWasSound(first: Boolean, second: Boolean) {
        wasFirstSound = first
        wasSecondSound = second
    }
}