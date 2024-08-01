package com.trestudio.periodtracker.viewmodel.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.trestudio.periodtracker.R

enum class TimelineButtonState {
    TimelineButton,
    ExitButton;

    @Composable
    fun image(): Painter {
        return painterResource(
            when (this) {
                TimelineButton -> R.drawable.view_timeline_48px
                ExitButton -> R.drawable.close_48px
            }
        )
    }

    fun getScreenState(): MainScreenState {
        return when (this) {
            TimelineButton -> MainScreenState.Timeline
            ExitButton -> MainScreenState.MainApp
        }
    }

    fun oposite(): TimelineButtonState {
        return when (this) {
            TimelineButton -> ExitButton
            ExitButton -> TimelineButton
        }
    }
}