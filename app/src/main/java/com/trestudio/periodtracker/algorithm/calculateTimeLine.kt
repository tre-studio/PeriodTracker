package com.trestudio.periodtracker.algorithm

import androidx.compose.ui.graphics.Color
import com.trestudio.periodtracker.components.layout.RangeDay
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import java.time.LocalDate

val PERIOD_COLOR = Color(0xffed6a5a) // kinh nguyet
val FERTILE_COLOR = Color(0xfff4f1bb) // thu thai
val OVULATION_COLOR = Color(0xff9bc1bc) // rung trung

fun calculateCyclePhases(lmpStartDate: LMPstartDate, currentMonth: LocalDate): List<RangeDay> {
    val phases = mutableListOf<RangeDay>()
    val cycleLength = lmpStartDate.avgCycle
    val periodLength = lmpStartDate.avgPeriod
    val ovulationStart = cycleLength / 2 - 2
//    val ovulationEnd = cycleLength / 2 + 2

    var currentStartDate = lmpStartDate.value

    while (currentStartDate.isBefore(currentMonth.plusMonths(1).withDayOfMonth(1))) {
        val periodStartDate = currentStartDate
        val periodEndDate = periodStartDate.plusDays(periodLength.toLong() - 1)
        phases.add(RangeDay(periodStartDate, periodEndDate, PERIOD_COLOR))

        val fertileStartDate = periodEndDate.plusDays(1)
        val fertileEndDate = fertileStartDate.plusDays(ovulationStart.toLong() - 1)
        phases.add(RangeDay(fertileStartDate, fertileEndDate, FERTILE_COLOR))

        val ovulationDate = fertileEndDate.plusDays(1)
        val ovulationEndDate = ovulationDate.plusDays(4)
        phases.add(RangeDay(ovulationDate, ovulationEndDate, OVULATION_COLOR))

//        val lutealStartDate = ovulationEndDate.plusDays(1)
//        val lutealEndDate = lutealStartDate.plusDays((cycleLength - (periodLength + ovulationEnd)).toLong() - 1)
//        phases.add(RangeDay(lutealStartDate, lutealEndDate, Color.Yellow))

        currentStartDate = periodEndDate.plusDays((cycleLength - periodLength).toLong())
    }

    return phases
}