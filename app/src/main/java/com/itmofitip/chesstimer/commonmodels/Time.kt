package com.itmofitip.chesstimer.commonmodels

data class Time(
    var minutes: Int = 0,
    var seconds: Int = 0,
    var millis: Int = 0,
    val increment: Time? = null
) {
    operator fun plus(other: Time): Time {
        addMillis(other.millis)
        addSeconds(other.seconds)
        addMinutes(other.minutes)
        return this
    }

    operator fun minus(other: Time): Time {
        subtractMillis(other.millis)
        subtractSeconds(other.seconds)
        subtractMinutes(other.minutes)
        return this
    }

    fun addMillis(millis: Int) {
        this.millis += millis
        if (this.millis >= 1000) {
            addSeconds(this.millis / 1000)
            this.millis = this.millis % 1000
        }
    }

    fun subtractMillis(millis: Int) {
        this.millis -= millis
        if (this.millis < 0) {
            subtractSeconds(1)
            this.millis += 1000
        }
    }

    fun addSeconds(seconds: Int) {
        this.seconds += seconds
        if (this.seconds >= 60) {
            addMinutes(this.seconds / 60)
            this.seconds = this.seconds % 60
        }
    }

    fun subtractSeconds(seconds: Int) {
        this.seconds -= seconds
        if (this.seconds < 0) {
            subtractMinutes(1)
            this.seconds += 60
        }
    }

    fun addMinutes(minutes: Int) {
        this.minutes += minutes
    }

    fun subtractMinutes(minutes: Int) {
        this.minutes -= minutes
    }
}
