package com.trestudio.periodtracker.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLayout(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
//                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}
