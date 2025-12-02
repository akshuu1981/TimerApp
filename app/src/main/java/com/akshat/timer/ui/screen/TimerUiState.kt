package com.akshat.timer.ui.screen

import com.akshat.timer.data.ScreenTypes

data class TimerUiState(
    val currentScreen: ScreenTypes = ScreenTypes.StopWatch,
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val laps: List<Long> = mutableListOf(),
    val timerString: String = if(currentScreen == ScreenTypes.StopWatch) "00:00:00.0" else "00:00:00"
)
