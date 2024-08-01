package com.trestudio.periodtracker.components.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.algorithm.FERTILE_COLOR
import com.trestudio.periodtracker.algorithm.OVULATION_COLOR
import com.trestudio.periodtracker.algorithm.PERIOD_COLOR
import com.trestudio.periodtracker.algorithm.calculateCyclePhases
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.components.layout.CalendarLayout
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.OrientationLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.layout.navigator.BottomNavigator
import com.trestudio.periodtracker.components.layout.titlebar.TitleBar
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.*
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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
    val phase = calculateCyclePhases(defaultLMP.value!!, currentLocalDate.value)
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
            when (mainScreenState.value) {
                MainScreenState.MainApp -> {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        val bigText: String
                        val smallText: String
                        val currentPhase = phase.find { LocalDate.now() in it.startDate..it.endDate }
                        if (currentPhase == null) {
                            bigText = stringResource(R.string.normal_days_big_text)
                            smallText = stringResource(R.string.normal_days_small_text)
                        } else {
                            when (currentPhase.color) {
                                PERIOD_COLOR -> {
                                    bigText = stringResource(R.string.period_phase_big_text)
                                    smallText = stringResource(R.string.period_phase_small_text)
                                }

                                FERTILE_COLOR -> {
                                    bigText = stringResource(R.string.fertile_phase_big_text)
                                    smallText = stringResource(R.string.fertile_phase_small_text)
                                }

                                OVULATION_COLOR -> {
                                    bigText = stringResource(R.string.ovulation_phase_big_text)
                                    smallText = stringResource(R.string.ovulation_phase_small_text)
                                }

                                else -> {
                                    bigText = stringResource(R.string.normal_days_big_text)
                                    smallText = stringResource(R.string.normal_days_small_text)
                                }
                            }
                        }

                        Text(
                            bigText,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp, lineHeight = 64.sp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(smallText, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(64.dp))
                    }
                    Box(contentAlignment = Alignment.BottomCenter) {
                        CalendarLayout(
                            10,
                            10,
                            rangeDays = phase,
                            viewModel
                        ) { day, note ->
                            Log.i("Test", "$day $note")
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentNote.value = note
                            currentDay.value = day
                            viewModel.setMainScreenState(MainScreenState.Note)
                            viewModel.setSettingButtonState(SettingButtonState.ReturnButton)
                        }
                    }
                }

                MainScreenState.Setting -> {
                    Column(modifier = Modifier.weight(1f)) {
                        BackHandler(enabled = true) {
                            currentNote.value = null
                            currentDay.value = null
                            viewModel.setMainScreenState(MainScreenState.MainApp)
                            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                        }
                        Text(text = "Setting", style = MaterialTheme.typography.headlineLarge)
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
                        Text(text = "Timeline", style = MaterialTheme.typography.headlineLarge)
                        Text(text = "Period phase", color = PERIOD_COLOR)
                        Text(text = "Fertile phase", color = FERTILE_COLOR)
                        Text(text = "Ovulation phase", color = OVULATION_COLOR)
                    }
                }

                MainScreenState.Note -> {
                    if (currentDay.value == null) {
                        currentNote.value = null
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                        return@OrientationLayout
                    }

                    var errorState by remember { mutableStateOf(false) }
                    val scrollState = rememberScrollState()
                    var crampsChecked by remember {
                        mutableStateOf(
                            currentNote.value?.symptom?.contains(Symptom.Cramps) ?: false
                        )
                    }
                    var fatigueChecked by remember {
                        mutableStateOf(
                            currentNote.value?.symptom?.contains(Symptom.Fatigue) ?: false
                        )
                    }
                    var moodChangesChecked by remember {
                        mutableStateOf(
                            currentNote.value?.symptom?.contains(Symptom.MoodChanges) ?: false
                        )
                    }
                    var selectedMood by remember {
                        mutableStateOf(
                            currentNote.value?.mood ?: Mood.Neutral
                        )
                    }
                    var number by remember {
                        mutableStateOf(
                            TextFieldValue(
                                text = if (currentNote.value == null) "" else currentNote.value!!.painLevel.toString(),
                            )
                        )
                    }
                    var note by remember {
                        mutableStateOf(
                            TextFieldValue(
                                text = currentNote.value?.otherNote ?: ""
                            )
                        )
                    }

                    BackHandler(enabled = true) {
                        currentNote.value = null
                        currentDay.value = null
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                    }

                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxSize(),
                    ) {
                        Text(
                            text = currentDay.value!!.date.format(
                                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(
                                    Locale.getDefault()
                                )
                            ),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
//                      Symptom
                        Column {
                            Text(
                                text = "Symptoms",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            CheckboxWithLabel(
                                checked = crampsChecked,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    crampsChecked = it
                                },
                                label = "Cramps"
                            )
                            CheckboxWithLabel(
                                checked = fatigueChecked,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    fatigueChecked = it
                                },
                                label = "Fatigue"
                            )
                            CheckboxWithLabel(
                                checked = moodChangesChecked,
                                onCheckedChange = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    moodChangesChecked = it
                                },
                                label = "Mood Changes"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
//                      MoodSection
                        Column {
                            Text(
                                text = "Mood",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Mood.entries.forEach { mood ->
                                RadioButtonWithLabel(
                                    selected = selectedMood == mood,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedMood = mood
                                    },
                                    label = mood.name
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
//                      Other notes
                        Column {
                            Text(
                                text = "Pain level",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BasicTextField(
                                value = number,
                                onValueChange = {
                                    if (it.text.isEmpty() || it.text.toIntOrNull() in 0..10) {
                                        number = it
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(100.dp)
                                    )
                                    .padding(16.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimaryContainer),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Transparent)
                                    ) {
                                        if (number.text.isEmpty()) {
                                            Text(
                                                "What is your pain level (0 to 10)",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            Text(
                                text = "Other Note",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BasicTextField(
                                value = note,
                                onValueChange = { note = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onPrimaryContainer,
                                        RoundedCornerShape(48.dp)
                                    )
                                    .padding(16.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimaryContainer),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Transparent)
                                    ) {
                                        if (note.text.isEmpty()) {
                                            Text(
                                                "Enter your note here",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                        ) {
                            if (currentNote.value != null) {
                                Row {
                                    BorderButton(onClick = {
                                        coroutineScope.launch {
                                            viewModel.deleteNote(currentNote.value!!)
                                            currentNote.value = null
                                            currentDay.value = null
                                            viewModel.setMainScreenState(MainScreenState.MainApp)
                                            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                                        }
                                    }, text = "Delete")
                                    Spacer(modifier = Modifier.width(16.dp))
                                    BorderButton(onClick = {
                                        val symptom = SymptonBuilder()
                                        if (crampsChecked) {
                                            symptom.withCramps()
                                        }

                                        if (fatigueChecked) {
                                            symptom.withFatigue()
                                        }

                                        if (moodChangesChecked) {
                                            symptom.withMoodChanges()
                                        }

                                        currentNote.value!!.otherNote = note.text
                                        currentNote.value!!.symptom = symptom.build()
                                        currentNote.value!!.painLevel = number.text.toInt()
                                        currentNote.value!!.mood = selectedMood

                                        if (number.text.toIntOrNull() !in 0..10) {
                                            errorState = true
                                            return@BorderButton
                                        }

                                        coroutineScope.launch {
                                            Log.i("Test", currentNote.value.toString())
                                            viewModel.updateNote(currentNote.value!!)
                                            currentNote.value = null
                                            currentDay.value = null
                                            viewModel.setMainScreenState(MainScreenState.MainApp)
                                            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                                        }
                                    }, text = "Update")
                                }
                            } else {
                                BorderButton(onClick = {
                                    val symptom = SymptonBuilder()
                                    if (crampsChecked) {
                                        symptom.withCramps()
                                    }

                                    if (fatigueChecked) {
                                        symptom.withFatigue()
                                    }

                                    if (moodChangesChecked) {
                                        symptom.withMoodChanges()
                                    }
                                    Log.i("Test", symptom.build().toString())

                                    if (number.text.toIntOrNull() !in 0..10) {
                                        errorState = true
                                        return@BorderButton
                                    }

                                    currentNote.value =
                                        NoteDB(
                                            date = currentDay.value!!.date,
                                            symptom = symptom.build(),
                                            painLevel = number.text.toInt(),
                                            otherNote = note.text,
                                            mood = selectedMood
                                        )

                                    coroutineScope.launch {
                                        Log.i(
                                            "Test",
                                            "${currentNote.value.toString()} $crampsChecked $fatigueChecked $moodChangesChecked"
                                        )
                                        if (currentNote.value == null) return@launch
                                        viewModel.addNote(currentNote.value!!)
                                        currentNote.value = null
                                        currentDay.value = null
                                        viewModel.setMainScreenState(MainScreenState.MainApp)
                                        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                                    }
                                }, text = "Create")
                            }
                        }
                    }

                    if (errorState) {
                        AlertDialog(
                            onDismissRequest = { errorState = false },
                            title = { Text("Hey you...") },
                            text = { Text("If you are not in pain, please answer with 0") },
                            confirmButton = {
                                Button(onClick = { errorState = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }

                MainScreenState.QRcode -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )

                    BackHandler(enabled = true) {
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                    }
                }

                MainScreenState.Help -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )

                    BackHandler(enabled = true) {
                        viewModel.setMainScreenState(MainScreenState.MainApp)
                    }
                }
            }
            BottomNavigator(mainScreenState, viewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = label)
    }
}

@Composable
fun RadioButtonWithLabel(
    selected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(text = label)
    }
}