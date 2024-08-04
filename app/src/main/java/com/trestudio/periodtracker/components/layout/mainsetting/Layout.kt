package com.trestudio.periodtracker.components.layout.mainsetting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.layout.CalendarLayout
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun SettingScreen(
    modifier: Modifier,
    viewModel: MainViewModel,
    defaultLMP: MutableState<LMPstartDate?>,
    haptic: HapticFeedback,
    coroutineScope: CoroutineScope
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) Setting@{
        BackHandler(enabled = true) {
            viewModel.setMainScreenState(MainScreenState.MainApp)
            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
        }
        Text(
            text = stringResource(R.string.setting_label),
            style = MaterialTheme.typography.headlineLarge
        )
        val currentData = defaultLMP.value ?: return@Setting
        var avgycleLengthState by remember {
            mutableStateOf(TextFieldValue(text = currentData.avgCycle.toString()))
        }
        var avgPeriodLengthState by remember {
            mutableStateOf(TextFieldValue(text = currentData.avgPeriod.toString()))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.getting_started_form_title1),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CalendarLayout(
            10, 0, viewModel = viewModel
        ) { value, _ ->
            val date = value.date
            if (date > LocalDate.now()) {
                return@CalendarLayout
            }
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            coroutineScope.launch {
                viewModel.completeLMPstartDate(
                    date,
                    currentData.avgCycle.toUInt(),
                    currentData.avgPeriod.toUInt()
                )
                defaultLMP.value = viewModel.getLMPstartDate()
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.getting_started_form_title2),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = avgycleLengthState,
            onValueChange = { newValue ->
                val filteredValue = newValue.text.filter { it.isDigit() }
                avgycleLengthState = TextFieldValue(
                    filteredValue, selection = newValue.selection
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    coroutineScope.launch {
                        viewModel.completeLMPstartDate(
                            currentData.value,
                            avgycleLengthState.text.toUInt(),
                            currentData.avgPeriod.toUInt()
                        )
                        defaultLMP.value = viewModel.getLMPstartDate()
                        defaultKeyboardAction(ImeAction.Next)
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.getting_started_form_title3),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = avgPeriodLengthState,
            onValueChange = { newValue ->
                val filteredValue = newValue.text.filter { it.isDigit() }
                avgPeriodLengthState = TextFieldValue(
                    filteredValue, selection = newValue.selection
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    coroutineScope.launch {
                        viewModel.completeLMPstartDate(
                            currentData.value,
                            currentData.avgCycle.toUInt(),
                            avgPeriodLengthState.text.toUInt()
                        )
                        defaultLMP.value = viewModel.getLMPstartDate()
                        defaultKeyboardAction(ImeAction.Done)
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                    }
                }
            )
        )
    }
}