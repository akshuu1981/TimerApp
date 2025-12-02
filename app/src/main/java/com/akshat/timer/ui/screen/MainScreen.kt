package com.akshat.timer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akshat.timer.R
import com.akshat.timer.data.ScreenItemContent
import com.akshat.timer.data.ScreenTypes
import com.akshat.timer.data.provider.getScreenContents
import com.akshat.timer.ui.theme.TimerTheme

@Composable
fun MainTimerScreen(
    viewModel: StopWatchViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
){
    val context = LocalContext.current
//    val uiState = viewModel.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ScreenTypes.valueOf(
        backStackEntry?.destination?.route ?: ScreenTypes.StopWatch.name
    )
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TimerTopBar() },
        bottomBar = { TimerBottomBar(
            currentTab = ScreenTypes.StopWatch,
            onTabPressed = {
                viewModel.updateCurrentScreen(it)
                navController.navigate(it.name,
                    navOptions = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build())
            },
            items = getScreenContents(context),
            modifier = Modifier.fillMaxWidth()
        ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenTypes.StopWatch.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(ScreenTypes.StopWatch.name){
              StopWatch(viewModel)
            }
            composable(route = ScreenTypes.Timer.name){
                TimerClock(viewModel)
            }
        }

    }
}


@Composable
fun TimerBottomBar(
    currentTab: ScreenTypes,
    onTabPressed: ((ScreenTypes) -> Unit),
    items: List<ScreenItemContent>,
    modifier: Modifier = Modifier
){
    NavigationBar(modifier = modifier){
        for(item in items){
            NavigationBarItem(
                selected = currentTab == item.type,
                onClick = { onTabPressed(item.type) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.text
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_action_timer),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
    )
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TimerTheme {
        MainTimerScreen()
    }
}