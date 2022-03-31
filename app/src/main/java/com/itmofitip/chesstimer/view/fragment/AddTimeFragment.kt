package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.itmofitip.chesstimer.R

class AddTimeFragment : Fragment() {

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
            with(findViewById<NumberPicker>(R.id.increment_hours_picker)) {
                minValue = 0
                maxValue = 99
            }
            with(findViewById<NumberPicker>(R.id.increment_minutes_picker)) {
                minValue = 0
                maxValue = 59
            }
            with(findViewById<NumberPicker>(R.id.game_time_seconds_picker)) {
                minValue = 0
                maxValue = 59
            }
        }
    }
}