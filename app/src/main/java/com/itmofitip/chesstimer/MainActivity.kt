package com.itmofitip.chesstimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itmofitip.chesstimer.repository.*
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.SavedDataInitializer
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
    val settingsSwitchesRepository = SettingsSwitchesRepository()

    private val savedDataInitializer = SavedDataInitializer(
        listOf(
            timeRepository, settingsSwitchesRepository, settingsTimeRepository
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        savedDataInitializer.initSavedData()
        supportActionBar?.hide()
        if (supportFragmentManager.backStackEntryCount == 0) {
            replaceFragment(TimerFragment(), false)
        }
    }

    override fun onStop() {
        super.onStop()
        savedDataInitializer.saveData()
    }
}