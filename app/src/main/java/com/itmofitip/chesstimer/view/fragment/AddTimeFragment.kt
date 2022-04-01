package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_time, container, false)
    }

    override fun onStart() {
        super.onStart()
        initPickers()
        initButtons()
    }

    override fun onSaveButtonClicked() {
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    override fun onWrongEnteredTime() {
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

    private fun initPickers() {
        with(requireActivity()) {
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
        }
    }
}