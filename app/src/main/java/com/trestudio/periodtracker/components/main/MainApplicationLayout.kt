package com.trestudio.periodtracker.components.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.algorithm.FERTILE_COLOR
import com.trestudio.periodtracker.algorithm.OVULATION_COLOR
import com.trestudio.periodtracker.algorithm.PERIOD_COLOR
import com.trestudio.periodtracker.algorithm.calculateCyclePhases
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.OrientationLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.layout.mainapp.MainApp
import com.trestudio.periodtracker.components.layout.mainnote.Note
import com.trestudio.periodtracker.components.layout.navigator.BottomNavigator
import com.trestudio.periodtracker.components.layout.titlebar.TitleBar
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState
import kotlinx.coroutines.CoroutineScope
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun MainApplicationLayout(portrait: Boolean, viewModel: MainViewModel, coroutineScope: CoroutineScope) {
    val haptic = LocalHapticFeedback.current
    val mainScreenState = viewModel.mainScreenState
    val defaultLMP: MutableState<LMPstartDate?> = remember {
        mutableStateOf(null)
    }
    val currentNote = remember { mutableStateOf<NoteDB?>(null) }
    val currentDay = remember { mutableStateOf<CalendarDay?>(null) }
    val currentLocalDate = viewModel.currentMonth

    LaunchedEffect(true) {
        defaultLMP.value = viewModel.getLMPstartDate()
        Log.i("Test", defaultLMP.value.toString())
    }

    if (defaultLMP.value == null) return
    val phase = calculateCyclePhases(defaultLMP.value!!, currentLocalDate.value, WeekFields.of(Locale.getDefault()))
    Log.i("Test", "Algorithm TEST: $phase")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        TitleBar(portrait, viewModel)
        OrientationLayout(
            verticalLayout = { VerticalLayout(it) },
            horizontalLayout = { HorizontalLayout(it) }
        ) {
            AnimatedContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                targetState = mainScreenState.value, label = "",
                transitionSpec = {
                    when (initialState.isInTheLeft(targetState)) {
                        true -> {
                            slideInHorizontally { w -> w } + fadeIn() togetherWith
                                    slideOutHorizontally { w -> -w / 2 } + fadeOut()
                        }

                        false -> {
                            slideInHorizontally { w -> -w } + fadeIn() togetherWith
                                    slideOutHorizontally { w -> w / 2 } + fadeOut()
                        }

                        else -> {
                            slideInHorizontally { w -> -w } + scaleIn(initialScale = 0.9F) + fadeIn() togetherWith
                                    slideOutHorizontally { w -> w } + scaleOut(targetScale = 0.5F) + fadeOut()
                        }
                    }
                }
            ) { targetState ->
                when (targetState) {
                    MainScreenState.MainApp -> {
                        MainApp(phase, viewModel, haptic, currentNote, currentDay)
                    }

                    MainScreenState.Note -> {
                        Note(currentDay, currentNote, viewModel, haptic, coroutineScope)
                    }

                    MainScreenState.Setting -> {
                        Column(modifier = Modifier.weight(1f)) {
                            BackHandler(enabled = true) {
                                currentNote.value = null
                                currentDay.value = null
                                viewModel.setMainScreenState(MainScreenState.MainApp)
                                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                            }
                            Text(
                                text = stringResource(R.string.setting_label),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(text = "Nothing for now")
                        }
                    }

                    MainScreenState.Timeline -> {
                        Column(modifier = Modifier.weight(1f)) {
                            BackHandler(enabled = true) {
                                currentNote.value = null
                                currentDay.value = null
                                viewModel.setMainScreenState(MainScreenState.MainApp)
                                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                            }
                            Text(
                                text = stringResource(R.string.timeline_label),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(text = "Period phase", color = PERIOD_COLOR)
                            Text(text = "Fertile phase", color = FERTILE_COLOR)
                            Text(text = "Ovulation phase", color = OVULATION_COLOR)
                        }
                    }

                    MainScreenState.QRcode -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(text = "AGRRHHH QRCODE")
                        }

                        BackToMainScreen(viewModel)
                    }

                    MainScreenState.Help -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(text = "AGRRHHH HELLLLP")
                        }

                        BackToMainScreen(viewModel)
                    }
                }
            }

            BottomNavigator(mainScreenState, viewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BackToMainScreen(viewModel: MainViewModel) {
    BackHandler(enabled = true) {
        viewModel.setMainScreenState(MainScreenState.MainApp)
    }
}
