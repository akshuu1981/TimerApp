package com.akshat.timer.data

import androidx.annotation.StringRes
import com.akshat.timer.R

enum class ScreenTypes (@StringRes val title: Int) {
    StopWatch(R.string.stopwatch),
    Timer(R.string.timer)
}