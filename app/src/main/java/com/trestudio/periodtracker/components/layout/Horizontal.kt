package com.trestudio.periodtracker.components.layout

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

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
