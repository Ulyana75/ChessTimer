package com.itmofitip.chesstimer.utilities

import android.media.MediaPlayer
import com.itmofitip.chesstimer.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SoundRouter {

    fun makeSoundClick() {
        makeSound(R.raw.timer_tap)
    }

    fun makeSoundLowTime() {
        makeSound(R.raw.low_time)
    }

    fun makeSoundFinished() {
        makeSound(R.raw.time_is_up)
    }

    private fun makeSound(soundId: Int) {
        val mp = MediaPlayer.create(APP_ACTIVITY, soundId)
        mp.setOnCompletionListener {
            mp.release()
        }
        mp.start()
    }
}