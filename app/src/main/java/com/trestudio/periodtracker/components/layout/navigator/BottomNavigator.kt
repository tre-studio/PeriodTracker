package com.trestudio.periodtracker.components.layout.navigator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
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
    val state = mainScreenState.value
    if (state != MainScreenState.Note) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        ) {
            NavButton(
                R.drawable.home_48px,
                R.string.home_button_description,
                R.string.nav_home_label,
                state.belongsToMainScreen(),
                Modifier.weight(1f)
            ) {
                viewModel.setMainScreenState(MainScreenState.MainApp)
                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
            }
            NavButton(
                R.drawable.qr_code_2_48px,
                R.string.qr_button_description,
                R.string.nav_qr_code_label,
                state == MainScreenState.QRcode,
                Modifier.weight(1f)
            ) {
                viewModel.setMainScreenState(MainScreenState.QRcode)
                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
            }
            NavButton(
                R.drawable.help_48px,
                R.string.help_button_description,
                R.string.nav_help_label,
                state == MainScreenState.Help,
                Modifier.weight(1f)
            ) {
                viewModel.setMainScreenState(MainScreenState.Help)
                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
            }
        }
    }
}
