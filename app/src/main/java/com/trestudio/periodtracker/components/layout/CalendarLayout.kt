package com.trestudio.periodtracker.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

data class RangeDay(val startDate: LocalDate, val endDate: LocalDate, val color: Color)

@Composable
fun CalendarLayout(
    offsetStart: Long,
    offsetEnd: Long,
    rangeDays: List<RangeDay> = listOf(),

    dayCallback: (CalendarDay) -> Unit,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(offsetStart) }
    val endMonth = remember { currentMonth.plusMonths(offsetEnd) }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val chosenMonth = state.firstVisibleMonth.yearMonth.month

    HorizontalCalendar(
        state = state,
        dayContent = { Day(it, dayCallback, rangeDays) },
        monthHeader = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(chosenMonth.toString(), modifier = Modifier.padding(start = 8.dp))
                DaysOfWeekTitle(daysOfWeek = daysOfWeek)
            }
        }
    )
}

@Composable
fun Day(day: CalendarDay, onClick: (CalendarDay) -> Unit, rangeDays: List<RangeDay>) {
    val color = MaterialTheme.colorScheme.onPrimaryContainer
    val outputColor = if (day.position == DayPosition.MonthDate) {
        color
    } else {
        color.copy(alpha = 0.5f)
    }

    val backgroundColor = if (day.date == LocalDate.now()) {
        // Set the background color to the current day's color
        MaterialTheme.colorScheme.primary
    } else {
        rangeDays.find { day.date in it.startDate..it.endDate }?.color
    }

    val shape = if (day.date == LocalDate.now()) {
        // Set the shape to CircleShape for the current day
        CircleShape
    } else if (backgroundColor != null) {
        val top = rangeDays.any { it.startDate == day.date }
        val bottom = rangeDays.any { it.endDate == day.date }
        RoundedCornerShape(
            topStartPercent = if (top) 50 else 0,
            bottomStartPercent = if (top) 50 else 0,
            topEndPercent = if (bottom) 50 else 0,
            bottomEndPercent = if (bottom) 50 else 0
        )
    } else {
        RoundedCornerShape(0)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor ?: Color.Transparent, shape)
            .clickable(onClick = { onClick(day) }),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier,
            color = if (backgroundColor == null) {outputColor} else {MaterialTheme.colorScheme.onPrimary},
            text = day.date.dayOfMonth.toString()
        )

//        Box(
//            modifier = Modifier.offset(y = 16.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(4.dp)
//                    .background(Color.Red, CircleShape)
//                    .padding(top = 100.dp)
//            )
//        }
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Preview
@Composable
fun CalendarLayoutPreview() {
    val currentDate = LocalDate.now()
    CalendarLayout(
        2, 2, listOf(
            RangeDay(currentDate.minusDays(10), currentDate.minusDays(5), Color.Red),
            RangeDay(currentDate, currentDate.plusDays(4), Color.Blue)
        )
    ) {

    }
}