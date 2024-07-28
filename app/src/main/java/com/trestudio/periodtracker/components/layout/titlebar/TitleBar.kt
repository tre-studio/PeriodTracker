package com.trestudio.periodtracker.components.layout.titlebar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R

@Composable
fun TitleBar(portrait: Boolean) {
    val timeInfo by currentTimeFlow().collectAsState(TimeInfo(0, ""))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(
                horizontal = if (portrait) 8.dp else 16.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.discover_tune_48px),
                contentDescription = "Settings",
                modifier = Modifier.size(36.dp),
            )
            TransitionName(
                listOf(stringResource(R.string.app_name), timeInfo.hourFormatted(), timeInfo.formattedDate)
            )
        }
        Icon(
            painter = painterResource(R.drawable.view_timeline_48px),
            contentDescription = "View TimeLine",
            modifier = Modifier.size(36.dp)
        )
    }
}