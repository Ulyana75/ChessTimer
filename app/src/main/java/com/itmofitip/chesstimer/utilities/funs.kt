package com.itmofitip.chesstimer.utilities

import androidx.fragment.app.Fragment
import com.itmofitip.chesstimer.R

fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
    if (addToBackStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment).addToBackStack(null)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit()
    }
}