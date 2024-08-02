package com.trestudio.periodtracker.components.layout.mainapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.algorithm.FERTILE_COLOR
import com.trestudio.periodtracker.algorithm.OVULATION_COLOR
import com.trestudio.periodtracker.algorithm.PERIOD_COLOR
import com.trestudio.periodtracker.components.layout.CalendarLayout
import com.trestudio.periodtracker.components.layout.DayTag
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import java.time.LocalDate

@Composable
fun MainApp(
    phase: HashMap<LocalDate, DayTag>,
    viewModel: MainViewModel,
    haptic: HapticFeedback,
    currentNote: MutableState<NoteDB?>,
    currentDay: MutableState<CalendarDay?>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxSize(),
        ) {
            val bigText: String
            val smallText: String
            val currentPhase = phase[LocalDate.now()]
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
                100,
                100,
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
}