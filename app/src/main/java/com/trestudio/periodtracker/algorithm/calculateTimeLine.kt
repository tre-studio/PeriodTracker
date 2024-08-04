package com.trestudio.periodtracker.algorithm

import androidx.compose.ui.graphics.Color
import com.trestudio.periodtracker.components.layout.DayTag
import com.trestudio.periodtracker.components.layout.DayTagKind
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

val PERIOD_COLOR = Color(0xffed6a5a) // kinh nguyet
val FERTILE_COLOR = Color(0xfff4f1bb) // thu thai
val OVULATION_COLOR = Color(0xff9bc1bc) // rung trung

fun calculateCyclePhases(
    lmpStartDate: LMPstartDate,
    currentMonth: LocalDate,
    weekFields: WeekFields
): HashMap<LocalDate, DayTag> {
    val result = HashMap<LocalDate, DayTag>()
    val refDate = lmpStartDate.value
    val avgCycleDay = lmpStartDate.avgCycle
    val avgPeriodDay = lmpStartDate.avgPeriod
    val (dayStart, dayEnd) = calendarDayRange(currentMonth, weekFields)

    for (d in dayStart..dayEnd) {
        val daysDiff = ChronoUnit.DAYS.between(refDate, d).toInt()
        val numberOfCycle = (daysDiff % avgCycleDay + avgCycleDay) % avgCycleDay

        val (color, phaseKind) = when {
            numberOfCycle < avgPeriodDay -> PERIOD_COLOR to when (numberOfCycle) {
                0 -> DayTagKind.Top
                avgPeriodDay - 1 -> DayTagKind.Bottom
                else -> DayTagKind.None
            }

            numberOfCycle < avgCycleDay - 16 -> FERTILE_COLOR to when (numberOfCycle) {
                avgPeriodDay -> DayTagKind.Top
                avgCycleDay - 17 -> DayTagKind.Bottom
                else -> DayTagKind.None
            }

            numberOfCycle < avgCycleDay - 11 -> OVULATION_COLOR to when (numberOfCycle) {
                avgCycleDay - 16 -> DayTagKind.Top
                avgCycleDay - 12 -> DayTagKind.Bottom
                else -> DayTagKind.None
            }

            else -> continue
        }

        result[d] = DayTag(phaseKind, color)
    }

    return result
}

fun calendarDayRange(currentMonth: LocalDate, weekFields: WeekFields): Pair<LocalDate, LocalDate> {
    val monthStart = currentMonth.withDayOfMonth(1)
    val monthEnd = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth())
    val dayStart = monthStart.with(TemporalAdjusters.previousOrSame(weekFields.firstDayOfWeek))
    val dayEnd = monthEnd.with(TemporalAdjusters.nextOrSame(weekFields.firstDayOfWeek.plus(6)))
    return Pair(dayStart, dayEnd)
}

// Reference: https://discuss.kotlinlang.org/t/range-expression-iterating-between-two-localdate-objects/16/2
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> {
    return object : Iterator<LocalDate> {
        private var next = this@iterator.start
        private val finalElement = this@iterator.endInclusive
        private var hasNext = !next.isAfter(this@iterator.endInclusive)
        override fun hasNext(): Boolean = hasNext
        override fun next(): LocalDate {
            val value = next
            if (value == finalElement) {
                hasNext = false
            } else {
                next = next.plusDays(1)
            }
            return value
        }
    }
}