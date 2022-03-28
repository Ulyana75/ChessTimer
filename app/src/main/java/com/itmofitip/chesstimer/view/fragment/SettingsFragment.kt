package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.presenter.SettingsPresenter
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.replaceFragment
import com.itmofitip.chesstimer.view.SettingsView
import com.itmofitip.chesstimer.view.TimeAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsFragment : Fragment(), SettingsView {

    private val presenter = SettingsPresenter(this)
    private lateinit var timeRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        timeRecyclerView = requireActivity().findViewById(R.id.time_recycler_view)
        timeRecyclerView.adapter = TimeAdapter(presenter.getTimeList(), presenter::onTimeItemChosen)
    }

    override fun onTimeItemChosen() {
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }
}