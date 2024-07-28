package com.trestudio.periodtracker.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalLayout(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 8.dp,
//                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        content()
    }
}
