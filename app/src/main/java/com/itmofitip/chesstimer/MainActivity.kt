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
import com.itmofitip.chesstimer.view.fragment.StartFragment
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
    val movesCountRepository = MovesCountRepository()
    val soundWasPlayedRepository = SoundWasPlayedRepository()

    val settingsTimeRepository = SettingsTimeRepository()
    val settingsSwitchesRepository = SettingsSwitchesRepository()

    private val savedDataInitializer = SavedDataInitializer(
        listOf(
            timeRepository, settingsSwitchesRepository, settingsTimeRepository
        )
    )

    private var themeChangesJob: Job? = null
    var needStartScreen = true

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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val firstMillisLeft = savedInstanceState.getLong(KEY_FIRST_MILLIS_LEFT)
        val secondMillisLeft = savedInstanceState.getLong(KEY_SECOND_MILLIS_LEFT)
        if (firstMillisLeft == 0L || secondMillisLeft == 0L) {
            soundWasPlayedRepository.soundOnFinishWasPlayed = true
        }
        timeRepository.setMillisLeft(
            firstMillisLeft,
            secondMillisLeft
        )
        movesCountRepository.setMovesCounts(
            savedInstanceState.getInt(KEY_MOVES_FIRST),
            savedInstanceState.getInt(KEY_MOVES_SECOND)
        )
        savedInstanceState.getString(KEY_PAUSE_STATE)?.let {
            pauseRepository.setPauseState(PauseState.valueOf(it))
        }
        savedInstanceState.getString(KEY_TURN)?.let {
            turnRepository.setTurn(Turn.valueOf(it))
        }
        needStartScreen = savedInstanceState.getBoolean(KEY_NEED_START_SCREEN)
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (needStartScreen) {
                replaceFragment(StartFragment(), false)
            }
            else {
                replaceFragment(TimerFragment(), false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (needStartScreen) {
                replaceFragment(StartFragment(), false)
            }
            else {
                replaceFragment(TimerFragment(), false)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_FIRST_MILLIS_LEFT, timeRepository.firstMillisLeft.value)
        outState.putLong(KEY_SECOND_MILLIS_LEFT, timeRepository.secondMillisLeft.value)
        outState.putInt(KEY_MOVES_FIRST, movesCountRepository.movesCountFirst.value)
        outState.putInt(KEY_MOVES_SECOND, movesCountRepository.movesCountSecond.value)
        outState.putString(
            KEY_PAUSE_STATE,
            if (pauseRepository.pauseState.value != PauseState.ACTIVE)
                pauseRepository.pauseState.value.toString()
            else PauseState.PAUSE.toString()
        )
        outState.putString(KEY_TURN, turnRepository.turn.value.toString())
        outState.putBoolean(KEY_NEED_START_SCREEN, needStartScreen)
    }

    override fun onStop() {
        super.onStop()
        savedDataInitializer.saveData()
        themeChangesJob?.cancel()
    }

    private fun collectThemeChanges(): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            settingsSwitchesRepository.isDarkThemeChecked.collect {
                val scrollY = findViewById<NestedScrollView>(R.id.scroll_view)?.scrollY
                when (it) {
                    true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                findViewById<NestedScrollView>(R.id.scroll_view)?.scrollTo(0, scrollY ?: 0)
            }
        }
    }

    companion object {
        private const val KEY_FIRST_MILLIS_LEFT = "first_millis_left"
        private const val KEY_SECOND_MILLIS_LEFT = "second_millis_left"
        private const val KEY_MOVES_FIRST = "moves_first"
        private const val KEY_MOVES_SECOND = "moves_second"
        private const val KEY_TURN = "turn"
        private const val KEY_PAUSE_STATE = "pause_state"
        private const val KEY_NEED_START_SCREEN = "need_start_screen"
    }
}