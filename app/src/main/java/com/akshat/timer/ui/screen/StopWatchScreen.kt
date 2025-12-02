package com.akshat.timer.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StopWatch(viewModel: StopWatchViewModel){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val textFieldValue = uiState.timerString
    val isRunning = uiState.isRunning

    val laps = uiState.laps

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TimerClock(textFieldValue, isLandscape)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.toggleStartStop()
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    enabled = true,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(if (isRunning) "Stop" else "Start")
                }
                Button(
                    onClick = {
                        viewModel.recordLap()
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    enabled = isRunning,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text("Lap")
                }
            }
        }

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .border(width = 2.dp,color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(laps) { lapTime ->
                LapTime(time = viewModel.formatTimer(lapTime))
            }
        }
    }
}

@Composable
fun LapTime(time: String){
    Text("Lap: $time",
            modifier = Modifier.padding(8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TimerClock(textFieldValue: String, isLandscape: Boolean) {
    TextField(
        value = textFieldValue,
        modifier = Modifier.fillMaxWidth(1f),
        textStyle = MaterialTheme.typography.displayLarge.copy(
            textAlign = TextAlign.Center,
            fontSize = if(isLandscape) MaterialTheme.typography.displayLarge.fontSize * 1.5f else MaterialTheme.typography.displayLarge.fontSize
        ),
        singleLine = true,
        onValueChange = { },
        readOnly = true,
        enabled = false,
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.background, // Or another color you prefer
            disabledIndicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f) // Customize indicator color
        )
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewStopWatch(){
    StopWatch(hiltViewModel())
}