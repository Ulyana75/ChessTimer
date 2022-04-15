package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.presenter.TimerPresenter
import com.itmofitip.chesstimer.utilities.animateVisibilityToGone
import com.itmofitip.chesstimer.utilities.animateVisibilityToInvisible
import com.itmofitip.chesstimer.utilities.animateVisibilityToVisible
import com.itmofitip.chesstimer.utilities.replaceFragment
import com.itmofitip.chesstimer.view.CircularProgressBar
import com.itmofitip.chesstimer.view.TimerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

const val LONG_LENGTH_TEXT_SIZE = 52
const val SHORT_LENGTH_TEXT_SIZE = 72

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
    }

    override fun onResume() {
        super.onResume()
        presenter.attach()
        initButtons()
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }

    private fun initButtons() {
        with(requireActivity()) {
            findViewById<ConstraintLayout>(R.id.button_timer_first)
                .setOnClickListener {
                    presenter.onFirstTimeButtonClicked()
                }
            findViewById<ConstraintLayout>(R.id.button_timer_second)
                .setOnClickListener {
                    presenter.onSecondTimeButtonClicked()
                }
            findViewById<ImageView>(R.id.pause_button).setOnClickListener {
                presenter.stopTimer()
            }
            findViewById<ImageView>(R.id.continue_button).setOnClickListener {
                presenter.startTimer()
            }
            findViewById<ImageView>(R.id.restart_button).setOnClickListener {
                presenter.restartTimer()
            }
            findViewById<ImageView>(R.id.settings_button).setOnClickListener {
                replaceFragment(SettingsFragment(), true)
            }
        }
    }

    override fun setFirstTime(time: String) {
        requireActivity().findViewById<TextView>(R.id.time_first).text = time
    }

    override fun setSecondTime(time: String) {
        requireActivity().findViewById<TextView>(R.id.time_second).text = time
    }

    override fun setFirstProgress(progress: Float) {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_first)
            .setProgress(progress)
    }

    override fun setSecondProgress(progress: Float) {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_second)
            .setProgress(progress)
    }

    override fun setFirstInactive() {
        requireActivity().findViewById<View>(R.id.inactive_first).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.inactive_second).visibility = View.GONE
    }

    override fun setSecondInactive() {
        requireActivity().findViewById<View>(R.id.inactive_first).visibility = View.GONE
        requireActivity().findViewById<View>(R.id.inactive_second).visibility = View.VISIBLE
    }

    override fun onNotStartedState() {
        with(requireActivity()) {
            findViewById<ImageView>(R.id.tap_to_start).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.settings_button).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.restart_button).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.continue_button).animateVisibilityToGone()
            findViewById<ImageView>(R.id.pause_button).animateVisibilityToGone()

            findViewById<View>(R.id.inactive_first).visibility = View.GONE
            findViewById<View>(R.id.inactive_second).visibility = View.GONE
        }
    }

    override fun onPauseState() {
        with(requireActivity()) {
            findViewById<ImageView>(R.id.tap_to_start).animateVisibilityToInvisible()
            findViewById<ImageView>(R.id.settings_button).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.restart_button).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.continue_button).animateVisibilityToVisible()
            findViewById<ImageView>(R.id.pause_button).animateVisibilityToGone()

            findViewById<View>(R.id.inactive_first).visibility = View.GONE
            findViewById<View>(R.id.inactive_second).visibility = View.GONE
        }
    }

    override fun onActiveState() {
        with(requireActivity()) {
            findViewById<ImageView>(R.id.tap_to_start).animateVisibilityToInvisible()
            findViewById<ImageView>(R.id.settings_button).animateVisibilityToGone()
            findViewById<ImageView>(R.id.restart_button).animateVisibilityToGone()
            findViewById<ImageView>(R.id.continue_button).animateVisibilityToGone()
            findViewById<ImageView>(R.id.pause_button).animateVisibilityToVisible()
        }
    }

    override fun onFirstFewTime() {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_first).setColor(
            requireActivity().resources.getColor(R.color.few_time)
        )
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_first)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_first).visibility = View.GONE
    }

    override fun onSecondFewTime() {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_second).setColor(
            requireActivity().resources.getColor(R.color.few_time)
        )
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_second)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_second).visibility = View.GONE
    }

    override fun onFirstFinished() {
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_first)
            .setBackgroundColor(
                requireActivity().resources.getColor(R.color.few_time)
            )
    }

    override fun onSecondFinished() {
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_second)
            .setBackgroundColor(
                requireActivity().resources.getColor(R.color.few_time)
            )
    }

    override fun onFirstNormal() {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_first).setColor(
            requireActivity().resources.getColor(R.color.main_blue)
        )
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_first)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_first).visibility = View.GONE
    }

    override fun onSecondNormal() {
        requireActivity().findViewById<CircularProgressBar>(R.id.progress_bar_second).setColor(
            requireActivity().resources.getColor(R.color.main_blue)
        )
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_second)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_second).visibility = View.GONE
    }

    override fun onFirstNeedMs() {
        onFirstFewTime()
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_first)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_first).visibility = View.VISIBLE
    }

    override fun onSecondNeedMs() {
        onSecondFewTime()
        requireActivity().findViewById<ConstraintLayout>(R.id.button_timer_second)
            .setBackgroundColor(getSecondaryColor())
        requireActivity().findViewById<TextView>(R.id.time_ms_second).visibility = View.VISIBLE
    }

    override fun setFirstMs(ms: String) {
        requireActivity().findViewById<TextView>(R.id.time_ms_first).text = ms
    }

    override fun setSecondMs(ms: String) {
        requireActivity().findViewById<TextView>(R.id.time_ms_second).text = ms
    }

    override fun onFirstShortTimeStr() {
        requireActivity().findViewById<TextView>(R.id.time_first).textSize =
            SHORT_LENGTH_TEXT_SIZE.toFloat()
    }

    override fun onSecondShortTimeStr() {
        requireActivity().findViewById<TextView>(R.id.time_second).textSize =
            SHORT_LENGTH_TEXT_SIZE.toFloat()
    }

    override fun onFirstLongTimeStr() {
        requireActivity().findViewById<TextView>(R.id.time_first).textSize =
            LONG_LENGTH_TEXT_SIZE.toFloat()
    }

    override fun onSecondLongTimeStr() {
        requireActivity().findViewById<TextView>(R.id.time_second).textSize =
            LONG_LENGTH_TEXT_SIZE.toFloat()
    }

    override fun onTimeOverflow() {
        with(requireContext()) {
            Toast.makeText(
                this,
                resources.getString(R.string.on_time_overflow),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getSecondaryColor(): Int {
        return TypedValue().apply {
            context?.theme?.resolveAttribute(R.attr.colorSecondary, this, true)
        }.data
    }
}