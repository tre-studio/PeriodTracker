package com.trestudio.periodtracker.viewmodel.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.trestudio.periodtracker.R

enum class SettingButtonState {
    SettingButton,
    ReturnButton;

    @Composable
    fun image(): Painter {
        return painterResource(
            when (this) {
                SettingButton -> R.drawable.discover_tune_48px
                ReturnButton -> R.drawable.chevron_left_48px
            }
        )
    }

    fun getScreenState(): MainScreenState {
        return when (this) {
            SettingButton -> MainScreenState.Setting
            ReturnButton -> MainScreenState.MainApp
        }
    }

    fun opposite(): SettingButtonState {
        return when (this) {
            SettingButton -> ReturnButton
            ReturnButton -> SettingButton
        }
    }
}