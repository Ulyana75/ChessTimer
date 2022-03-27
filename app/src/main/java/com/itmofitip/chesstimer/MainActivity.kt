package com.itmofitip.chesstimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itmofitip.chesstimer.view.fragment.TimerFragment
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.replaceFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        replaceFragment(TimerFragment(), false)
    }
}