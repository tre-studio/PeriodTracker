package com.trestudio.periodtracker.components.button

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BorderButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer, RoundedCornerShape(100.dp)),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        onClick = onClick
    ) {
        Text(text, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}