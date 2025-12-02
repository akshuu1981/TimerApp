package com.akshat.timer.data.provider

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.akshat.timer.R
import com.akshat.timer.data.ScreenItemContent
import com.akshat.timer.data.ScreenTypes

fun getScreenContents(context: Context): List<ScreenItemContent> {
    val screenContents = listOf(
        ScreenItemContent(
            text = context.getString(R.string.stopwatch),
            icon = R.drawable.ic_action_timer, type = ScreenTypes.StopWatch
        ),
        ScreenItemContent(
            text = context.getString(R.string.timer),
            icon = R.drawable.ic_action_access_time, type = ScreenTypes.Timer
        )
    )
    return screenContents
}