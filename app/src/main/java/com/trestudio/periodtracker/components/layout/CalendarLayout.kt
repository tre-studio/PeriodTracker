package com.trestudio.periodtracker.components.layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

enum class DayTagKind {
    Top, Bottom, None;
}

class DayTag(val kind: DayTagKind, val color: Color)

val currentDay: LocalDate = LocalDate.now()

@Composable
fun CalendarLayout(
    offsetStart: Long,
    offsetEnd: Long,
    rangeDays: HashMap<LocalDate, DayTag> = hashMapOf(),
    viewModel: MainViewModel,
    dayCallback: (CalendarDay, NoteDB?) -> Unit,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(offsetStart) }
    val endMonth = remember { currentMonth.plusMonths(offsetEnd) }
    val daysOfWeek = remember { daysOfWeek() }
    val noteDays = remember { mutableStateOf(listOf<NoteDB>()) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val chosenMonth = state.firstVisibleMonth.yearMonth.format(DateTimeFormatter.ofPattern("MMMM / yyyy"))

    viewModel.setCurrentMonth(state.firstVisibleMonth.yearMonth.atDay(1))

    LaunchedEffect(state.firstVisibleMonth) {
        Log.i("Test", "Start fetching Note ${state.firstVisibleMonth.yearMonth.atDay(1)}")
        noteDays.value = viewModel.getNotesForMonth(state.firstVisibleMonth.yearMonth.atDay(1))
        Log.i("Test", "Start fetching Note RESULT ${noteDays.value}")
    }

    HorizontalCalendar(
        state = state,
        dayContent = { Day(it, dayCallback, rangeDays, noteDays.value) },
        monthHeader = {
            Column {
                Text(
                    chosenMonth,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}

@Composable
private fun Day(
    day: CalendarDay,
    onClick: (CalendarDay, NoteDB?) -> Unit,
    rangeDays: HashMap<LocalDate, DayTag>,
    noteDays: List<NoteDB>
) {
    val note = noteDays.find { it.date == day.date }
    val sameDay = day.date == currentDay
    val searchResult = rangeDays[day.date]
    val backgroundColor = if (sameDay) {
        MaterialTheme.colorScheme.primary
    } else {
        searchResult?.color
    }

    val color = MaterialTheme.colorScheme.onPrimaryContainer
    val outputColor =
        (if (backgroundColor == null) color else if (backgroundColor.luminance() > 0.5) Color.Black else Color.White)
            .let { if (day.position != DayPosition.MonthDate) it.copy(alpha = 0.5f) else it }

    val top = searchResult?.kind == DayTagKind.Top
    val bottom = searchResult?.kind == DayTagKind.Bottom

    val shape = if (searchResult == null) {
        CircleShape
    } else {
        RoundedCornerShape(
            topStartPercent = if (top) 50 else 0,
            bottomStartPercent = if (top) 50 else 0,
            topEndPercent = if (bottom) 50 else 0,
            bottomEndPercent = if (bottom) 50 else 0
        )
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor ?: Color.Transparent, shape)
            .clickable(onClick = { onClick(day, note) }),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier,
            color = outputColor,
            text = day.date.dayOfMonth.toString()
        )

        if (note != null) {
            Box(
                modifier = Modifier.offset(y = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(outputColor, CircleShape)
                        .padding(top = 100.dp)
                )
            }
        }
    }
}

@Composable
private fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
