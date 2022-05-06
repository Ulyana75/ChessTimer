package com.itmofitip.chesstimer.repository

class SoundWasPlayedRepository {
    var soundOnFinishWasPlayed = false

    fun reset() {
        soundOnFinishWasPlayed = false
    }
}