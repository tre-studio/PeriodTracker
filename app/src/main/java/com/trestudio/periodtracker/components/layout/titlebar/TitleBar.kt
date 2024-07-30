package com.trestudio.periodtracker.components.layout.titlebar

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState

@Composable
fun TitleBar(portrait: Boolean, viewModel: MainViewModel) {
    val timeInfo by currentTimeFlow().collectAsState(TimeInfo(0, ""))
    val settingButton = viewModel.settingButtonState
    val timelineButton = viewModel.timelineButtonState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(
                horizontal = if (portrait) 8.dp else 16.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = settingButton.value.image(), transitionSpec = {
                    fadeIn() + slideInHorizontally(initialOffsetX = { width -> width }) togetherWith slideOutHorizontally(
                        targetOffsetX = { width -> -width }) + fadeOut()
                }, label = ""
            ) { painter ->
                Icon(
                    painter = painter,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            if (settingButton.value == SettingButtonState.SettingButton
                                && timelineButton.value == TimelineButtonState.ExitButton
                            ) {
                                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                            }
                            viewModel.setMainScreenState(settingButton.value.getScreenState())
                            viewModel.switchSettingButtonState()
                        },
                )
            }

            TransitionName(
                listOf(stringResource(R.string.app_name), timeInfo.hourFormatted(), timeInfo.formattedDate)
            )
        }

        AnimatedContent(
            targetState = timelineButton.value.image(), transitionSpec = {
                fadeIn() + slideInHorizontally(initialOffsetX = { width -> -width }) togetherWith slideOutHorizontally(
                    targetOffsetX = { width -> width }) + fadeOut()
            }, label = ""
        ) { painter ->
            Icon(
                painter = painter,
                contentDescription = "View TimeLine",
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        if (timelineButton.value == TimelineButtonState.TimelineButton
                            && settingButton.value == SettingButtonState.ReturnButton
                        ) {
                            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                        }
                        viewModel.setMainScreenState(timelineButton.value.getScreenState())
                        viewModel.switchTimelineButtonState()
                    },
            )
        }
    }
}