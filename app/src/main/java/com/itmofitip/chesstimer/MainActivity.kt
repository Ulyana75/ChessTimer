package com.itmofitip.chesstimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itmofitip.chesstimer.repository.PauseRepository
import com.itmofitip.chesstimer.repository.SettingsTimeRepository
import com.itmofitip.chesstimer.repository.TimeQuantityRepository
import com.itmofitip.chesstimer.repository.TimeRepository
import com.itmofitip.chesstimer.repository.TurnRepository
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.replaceFragment
import com.itmofitip.chesstimer.view.fragment.TimerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    val turnRepository = TurnRepository()
    val pauseRepository = PauseRepository()
    val timeRepository = TimeRepository()
    val timeQuantityRepository = TimeQuantityRepository()

    val settingsTimeRepository = SettingsTimeRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        timeRepository.initStartTime()
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        replaceFragment(TimerFragment(), false)
    }
}