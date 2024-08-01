package com.trestudio.periodtracker.components.layout.titlebar

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.trestudio.periodtracker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

data class TimeInfo(
    val hours: Int,
    val formattedDate: String
) {
    @Composable
    fun hourFormatted(): String = when (hours) {
        in 5..11 -> stringResource(R.string.good_morning)
        in 12..18 -> stringResource(R.string.good_afternoon)
        in 19..22 -> stringResource(R.string.good_evening)
        in 22..23, in 0..4 -> stringResource(R.string.good_night)
        else -> ""
    }
}

fun currentTimeFlow(): Flow<TimeInfo> = flow {
    val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat("EEE, dd/MM", locale)
    if (locale.country == "US") {
        dateFormat.applyPattern("EEE MM/dd")
    }

    while (true) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val formattedDate = dateFormat.format(calendar.time)
        emit(TimeInfo(hour, formattedDate))
        delay(60000)
    }
}