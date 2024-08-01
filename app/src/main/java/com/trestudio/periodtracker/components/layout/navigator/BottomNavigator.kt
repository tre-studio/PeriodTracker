package com.trestudio.periodtracker.components.layout.navigator

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState

// Todo: Clean code
@Composable
fun BottomNavigator(
    mainScreenState: State<MainScreenState>,
    viewModel: MainViewModel
) {
    val haptic = LocalHapticFeedback.current
    val state = mainScreenState.value
    if (state != MainScreenState.Note) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxSize()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                        viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            if (state.belongsToMainScreen()) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                Color.Transparent
                            },
                            CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.home_48px),
                        contentDescription = "Home button",
                    )
                    if (state.belongsToMainScreen()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = "Home"
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxSize()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setMainScreenState(MainScreenState.QRcode)
                        viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            if (state == MainScreenState.QRcode) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                Color.Transparent
                            },
                            CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.qr_code_2_48px),
                        contentDescription = "QR button",
                    )
                    if (state == MainScreenState.QRcode) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = "QR code"
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxSize()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setMainScreenState(MainScreenState.Help)
                        viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            if (state == MainScreenState.Help) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                Color.Transparent
                            },
                            CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.help_48px),
                        contentDescription = "Help button",
                    )
                    if (state == MainScreenState.Help) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = "Help"
                        )
                    }
                }
            }
        }
    }
}
