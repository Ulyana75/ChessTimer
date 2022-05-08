package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.presenter.AddTimePresenter
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.view.AddTimeView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class AddTimeFragment : Fragment(), AddTimeView {

    private val presenter = AddTimePresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_time, container, false)
        initPickers(view, savedInstanceState)
        return view
    }

    override fun onStart() {
        super.onStart()
        initButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(requireActivity()) {
            outState.putInt(
                KEY_GAME_HOURS,
                findViewById<NumberPicker>(R.id.game_time_hours_picker).value
            )
            outState.putInt(
                KEY_GAME_MINUTES,
                findViewById<NumberPicker>(R.id.game_time_minutes_picker).value
            )
            outState.putInt(
                KEY_GAME_SECONDS,
                findViewById<NumberPicker>(R.id.game_time_seconds_picker).value
            )
            outState.putInt(
                KEY_INCREMENT_MINUTES,
                findViewById<NumberPicker>(R.id.increment_minutes_picker).value
            )
            outState.putInt(
                KEY_INCREMENT_SECONDS,
                findViewById<NumberPicker>(R.id.increment_seconds_picker).value
            )
        }
    }

    override fun onSaveButtonClicked() {
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    override fun onWrongEnteredTime() {
        with(requireContext()) {
            Toast.makeText(
                this,
                resources.getString(R.string.on_wrong_time_entered),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initButtons() {
        with(requireActivity()) {
            findViewById<ImageView>(R.id.return_button_2).setOnClickListener {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
            findViewById<TextView>(R.id.save_time_button).setOnClickListener {
                presenter.onSaveButtonClicked(
                    findViewById<NumberPicker>(R.id.game_time_hours_picker).value,
                    findViewById<NumberPicker>(R.id.game_time_minutes_picker).value,
                    findViewById<NumberPicker>(R.id.game_time_seconds_picker).value,
                    findViewById<NumberPicker>(R.id.increment_minutes_picker).value,
                    findViewById<NumberPicker>(R.id.increment_seconds_picker).value,
                )
            }
        }
    }

    private fun initPickers(view: View, savedInstanceState: Bundle?) {
        with(view) {
            with(findViewById<NumberPicker>(R.id.game_time_hours_picker)) {
                minValue = 0
                maxValue = 99
            }
            with(findViewById<NumberPicker>(R.id.game_time_minutes_picker)) {
                minValue = 0
                maxValue = 59
            }
            with(findViewById<NumberPicker>(R.id.game_time_seconds_picker)) {
                minValue = 0
                maxValue = 59
            }
            with(findViewById<NumberPicker>(R.id.increment_minutes_picker)) {
                minValue = 0
                maxValue = 59
            }
            with(findViewById<NumberPicker>(R.id.increment_seconds_picker)) {
                minValue = 0
                maxValue = 59
            }

            if (savedInstanceState != null) {
                findViewById<NumberPicker>(R.id.game_time_hours_picker).value =
                    savedInstanceState.getInt(KEY_GAME_HOURS)
                findViewById<NumberPicker>(R.id.game_time_minutes_picker).value =
                    savedInstanceState.getInt(KEY_GAME_MINUTES)
                findViewById<NumberPicker>(R.id.game_time_seconds_picker).value =
                    savedInstanceState.getInt(KEY_GAME_SECONDS)
                findViewById<NumberPicker>(R.id.increment_minutes_picker).value =
                    savedInstanceState.getInt(KEY_INCREMENT_MINUTES)
                findViewById<NumberPicker>(R.id.increment_seconds_picker).value =
                    savedInstanceState.getInt(KEY_INCREMENT_SECONDS)
            }
        }
    }

    companion object {
        private const val KEY_GAME_HOURS = "game_hours"
        private const val KEY_GAME_MINUTES = "game_minutes"
        private const val KEY_GAME_SECONDS = "game_seconds"
        private const val KEY_INCREMENT_MINUTES = "increment_minutes"
        private const val KEY_INCREMENT_SECONDS = "increment_seconds"
    }
}