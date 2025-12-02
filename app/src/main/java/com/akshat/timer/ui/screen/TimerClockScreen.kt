package com.akshat.timer.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun TimerClock(viewModel: StopWatchViewModel){

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val textFieldValue = uiState.value.timerString

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TimerClockUI(textFieldValue, isLandscape)

            // 1. Define the layout of the number pad
            val buttonLayout = listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9)
            )

            // 2. Outer loop creates a Row for each inner list
            buttonLayout.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp), // Only horizontal padding needed here
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between buttons
                ) {
                    // 3. Inner loop creates a Button for each number in the row
                    rowItems.forEach { number ->
                        NumberButton(
                            number = number,
                            onClick = { viewModel.addTime(number) },
                            modifier = Modifier.weight(1f) // Each button takes equal space
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                NumberButton(number = 0, onClick = { viewModel.addTime(0) })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        viewModel.beginCountDown()
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .padding(8.dp).align(Alignment.CenterVertically),
                    enabled = true,

                    shape = RoundedCornerShape(16.dp),
                ) {

                    Text(text = "Begin")
                }

            }
        }
    }
}

@Composable
fun NumberButton(
    number: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(vertical = 4.dp), // Adjust padding as needed
        shape = RoundedCornerShape(16.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Text(text = number.toString())
    }
}

@Composable
fun TimerClockUI(textFieldValue: String, isLandscape: Boolean) {
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
fun PreviewTimerClock(){
    TimerClock(hiltViewModel())
}
