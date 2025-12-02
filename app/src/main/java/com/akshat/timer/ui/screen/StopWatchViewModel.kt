package com.akshat.timer.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshat.timer.data.ScreenTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
): ViewModel() {

    val DELAY_TIMER = 100L
    val COUNTDOWN_TIMER = 1000L

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            startStopWatch()
        }
    }

    private suspend fun startStopWatch() {
        while (true) {
            if (_uiState.value.isRunning) {
                delay(DELAY_TIMER) // Delay for 100 millisecond
                val time = (_uiState.value.elapsedTime + DELAY_TIMER)
                _uiState.update {
                    it.copy(
                        elapsedTime = time,
                        timerString = formatTimer(time)
                    )
                }
            } else {
                delay(10)
            }
        }
    }

    fun updateCurrentScreen(screenType: ScreenTypes){
        _uiState.compareAndSet(_uiState.value, TimerUiState())
        _uiState.update {
            it.copy(
                currentScreen = screenType)
        }
        Log.d("TimerClock", "Changing screen & uistate now is ${_uiState.value}")
    }

    fun toggleStartStop() {
        if(_uiState.value.isRunning) {
            _uiState.update { it.copy(isRunning = false) }
        }else{
            if(_uiState.value.elapsedTime >= 0L && !_uiState.value.isRunning) {
                _uiState.update {
                    it.copy(
                        isRunning = true,
                        elapsedTime = 0L,
                        laps = emptyList()
                    )
                }
            }
        }
    }

    fun recordLap() {
        if(_uiState.value.isRunning){
            val currentLaps = _uiState.value.laps.toMutableList()
            currentLaps.add(0,_uiState.value.elapsedTime)
            _uiState.update {
                it.copy(laps = currentLaps)
            }
        }
    }

    fun formatTimer(timerMillis: Long, displayMillis: Boolean = true): String {
        val hours = (timerMillis / (1000 * 60 * 60)) % 24
        val minutes = (timerMillis / (1000 * 60)) % 60
        val seconds = (timerMillis / 1000) % 60
        val milliseconds = (timerMillis % 1000) / DELAY_TIMER
        if(displayMillis)
        return String.format("%02d:%02d:%02d.%01d", hours, minutes, seconds, milliseconds)
        else return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    fun addTime(num: Int) {
        var newValue = _uiState.value.elapsedTime
        Log.d("TimerClock", "addTime: $newValue to $newValue && shifting right gives ${newValue*10}")
        newValue =(newValue*10).plus(num)
        _uiState.update {
            it.copy(
                timerString = newValue.toString(),
                elapsedTime = newValue
            )
        }
        Log.d("TimerClock", "uistate now is ${_uiState.value}")
    }

    fun beginCountDown(){
        viewModelScope.launch {
            startCountDown()
        }
    }

    private suspend fun startCountDown() {
        Log.d("TimerClock", "beginCountDown: ${_uiState.value.timerString}")
        while (true) {
            delay(COUNTDOWN_TIMER) // Delay for 100 millisecond
            val time = ((_uiState.value.elapsedTime) - COUNTDOWN_TIMER/1000L)
            _uiState.update {
                it.copy(
                    elapsedTime = time,
                    timerString = formatTimer(time*1000, false)
                )
            }
            if(_uiState.value.elapsedTime <= 0)
                break
        }
    }
}