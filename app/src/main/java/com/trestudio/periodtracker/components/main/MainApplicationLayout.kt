package com.trestudio.periodtracker.components.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.algorithm.FERTILE_COLOR
import com.trestudio.periodtracker.algorithm.OVULATION_COLOR
import com.trestudio.periodtracker.algorithm.PERIOD_COLOR
import com.trestudio.periodtracker.algorithm.calculateCyclePhases
import com.trestudio.periodtracker.components.camera.ScanCamera
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.OrientationLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.layout.mainapp.MainApp
import com.trestudio.periodtracker.components.layout.mainnote.Note
import com.trestudio.periodtracker.components.layout.mainsetting.SettingScreen
import com.trestudio.periodtracker.components.layout.navigator.BottomNavigator
import com.trestudio.periodtracker.components.layout.notetimeline.NoteList
import com.trestudio.periodtracker.components.layout.titlebar.TitleBar
import com.trestudio.periodtracker.components.qr.ShareQRcode
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun MainApplicationLayout(
    portrait: Boolean,
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    permissionRequest: ActivityResultLauncher<String>
) {
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
                        SettingScreen(Modifier.weight(1f), viewModel, defaultLMP, haptic, coroutineScope)
                    }

                    MainScreenState.Timeline -> {
                        var notes by remember { mutableStateOf(emptyList<NoteDB>()) }
                        LaunchedEffect(true) {
                            notes = viewModel.getAllNotes()
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            BackHandler(enabled = true) {
                                viewModel.setMainScreenState(MainScreenState.MainApp)
                                viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                                viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                            }
                            Text(
                                text = stringResource(R.string.timeline_label),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            NoteList(notes) {
                                currentDay.value = localDateToCalendarDay(it.date)
                                currentNote.value = it
                                viewModel.setMainScreenState(MainScreenState.Note)
                            }
                        }
                    }

                    MainScreenState.QRcode -> {
                        ShareQRcode(defaultLMP, viewModel)
                    }

                    MainScreenState.QRcodeCamera -> {
                        ScanCamera(permissionRequest, coroutineScope, viewModel, defaultLMP)
                    }

                    MainScreenState.Help -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = stringResource(R.string.help),
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Getting Started",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This is menstrual tracker app",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "How to use",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Click around and find out",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                ColorAndText(PERIOD_COLOR, "Menstrual Phase: The time when menstrual bleeding occurs.")
                                Spacer(modifier = Modifier.height(8.dp))
                                ColorAndText(
                                    FERTILE_COLOR,
                                    "Fertile Window: The days when you're most likely to conceive if you have unprotected sex."
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ColorAndText(
                                    OVULATION_COLOR,
                                    "Ovulation Period: The phase when an egg is released from the ovary."
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ColorAndText(MaterialTheme.colorScheme.onPrimaryContainer, "Current day.")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "- If the day has dot below the number, that means you have a note on that day. You can edit it or delete it if you want\n" +
                                        "- You can create a new note by simply click on the day you want.\n" +
                                        "❤️ Have fun and stay healthy!",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "FAQs",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Privacy: No tracker no anything. Just a simple app does simple things.",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = "Sorry my deadline prevented me from write a better help section.",
                                style = MaterialTheme.typography.bodySmall,
                            )
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
fun BackToMainScreen(viewModel: MainViewModel) {
    BackHandler(enabled = true) {
        viewModel.setMainScreenState(MainScreenState.MainApp)
    }
}

@Composable
fun ColorAndText(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

private fun localDateToCalendarDay(localDate: LocalDate): CalendarDay {
    return CalendarDay(localDate, DayPosition.MonthDate)
}
