package com.itmofitip.chesstimer

import android.os.Bundle
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import com.itmofitip.chesstimer.repository.*
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.SavedDataInitializer
import com.itmofitip.chesstimer.utilities.replaceFragment
import com.itmofitip.chesstimer.view.fragment.TimerFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


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

    private var themeChangesJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        savedDataInitializer.initSavedData()
        when (settingsSwitchesRepository.isDarkThemeChecked.value) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        themeChangesJob = collectThemeChanges()
        if (supportFragmentManager.backStackEntryCount == 0) {
            replaceFragment(TimerFragment(), false)
        }
    }

    override fun onStop() {
        super.onStop()
        savedDataInitializer.saveData()
        themeChangesJob?.cancel()
    }

    private fun collectThemeChanges(): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            settingsSwitchesRepository.isDarkThemeChecked.collect {
                when (it) {
                    true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                findViewById<NestedScrollView>(R.id.scroll_view)?.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }
}