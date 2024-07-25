package com.trestudio.periodtracker.components.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalLayout() {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
//        Todo: component
        Text(text = "Good morning")
    }
}