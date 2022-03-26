package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.presenter.TimerPresenter
import com.itmofitip.chesstimer.view.CircularProgressBar
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class TimerFragment : Fragment(), TimerView {

    private lateinit var presenter: TimerPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onStart() {
        super.onStart()

        presenter = TimerPresenter(this)

        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_first).setOnClickListener {
            presenter.onFirstTimeButtonClicked()
        }
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_second).setOnClickListener {
            presenter.onSecondTimeButtonClicked()
        }
    }

    override fun setFirstTime(time: String) {
        requireActivity().findViewById<TextView>(R.id.time_first).text = time
    }

    override fun setSecondTime(time: String) {
        requireActivity().findViewById<TextView>(R.id.time_second).text = time
    }

    override fun setFirstProgress(progress: Float) {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_first).setProgress(progress)
    }

    override fun setSecondProgress(progress: Float) {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_second).setProgress(progress)
    }
}