package com.itmofitip.chesstimer.utilities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.fragment.app.Fragment
import com.itmofitip.chesstimer.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
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

fun getNormalizedTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis - TimeUnit.HOURS.toMillis(hours))
    val seconds = TimeUnit.MILLISECONDS.toSeconds(
        millis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes)
    )
    val strMinutes = if (minutes < 10) "0$minutes" else "$minutes"
    val strSeconds = if (seconds < 10) "0$seconds" else "$seconds"
    val strHours = if (hours < 10) "0$hours" else "$hours"
    return if (hours == 0L) "$strMinutes:$strSeconds"
    else "$strHours:$strMinutes:$strSeconds"
}

fun View.animateVisibilityToVisible() {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(ANIMATION_DURATION).setListener(null)
}

fun View.animateVisibilityToInvisible() {
    animate().alpha(0f).setDuration(ANIMATION_DURATION).setListener(null)
}

fun View.animateVisibilityToGone() {
    animate().alpha(0f).setDuration(ANIMATION_DURATION).setListener(
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        }
    )
}

fun Int.toStringWithTwoDigits(): String {
    return if (this < 10) "0$this" else "$this"
}