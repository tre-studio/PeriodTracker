package com.trestudio.periodtracker.components.layout.mainnote

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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.Mood
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import com.trestudio.periodtracker.viewmodel.database.Symptom
import com.trestudio.periodtracker.viewmodel.database.SymptonBuilder
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@Composable
fun Note(
    currentDay: MutableState<CalendarDay?>,
    currentNote: MutableState<NoteDB?>,
    viewModel: MainViewModel,
    haptic: HapticFeedback,
    coroutineScope: CoroutineScope
) {
    val scrollState = rememberScrollState()

    BackHandler(enabled = true) {
        viewModel.setMainScreenState(MainScreenState.MainApp)
        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
    ) {
        if (currentDay.value == null) {
            return@Column
        }

        var errorState by remember { mutableStateOf(false) }
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
                text = stringResource(R.string.symptoms),
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            CheckboxWithLabel(
                checked = crampsChecked,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    crampsChecked = it
                },
                label = stringResource(R.string.cramps)
            )
            CheckboxWithLabel(
                checked = fatigueChecked,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    fatigueChecked = it
                },
                label = stringResource(R.string.fatigue)
            )
            CheckboxWithLabel(
                checked = moodChangesChecked,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    moodChangesChecked = it
                },
                label = stringResource(R.string.mood_changes)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
//                      MoodSection
        Column {
            Text(
                text = stringResource(R.string.mood),
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
                    label = when (mood) {
                        Mood.Happy -> stringResource(R.string.happy)
                        Mood.Neutral -> stringResource(R.string.neutral)
                        Mood.Sad -> stringResource(R.string.sad)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
//                      Other notes
        Column {
            Text(
                text = stringResource(R.string.pain_level),
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
                                stringResource(R.string.what_is_your_pain_level_0_to_10),
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
                text = stringResource(R.string.other_note),
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
                                stringResource(R.string.enter_your_note_here),
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
                            viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                        }
                    }, text = stringResource(R.string.note_delete_button))
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
                            viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                        }
                    }, text = stringResource(R.string.note_update_button))
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
                        viewModel.setTimelineButtonState(TimelineButtonState.TimelineButton)
                    }
                }, text = stringResource(R.string.note_create_button))
            }
        }

        if (errorState) {
            AlertDialog(
                onDismissRequest = { errorState = false },
                title = { Text("Hey you...") },
                text = { Text(stringResource(R.string.if_you_are_not_in_pain_please_answer_with_0)) },
                confirmButton = {
                    Button(onClick = { errorState = false }) {
                        Text("OK")
                    }
                }
            )
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