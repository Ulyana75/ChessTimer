package com.itmofitip.chesstimer.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.replaceFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<TextView>(R.id.start_button).setOnClickListener {
            replaceFragment(TimerFragment(), false)
            APP_ACTIVITY.needStartScreen = false
        }
    }
}