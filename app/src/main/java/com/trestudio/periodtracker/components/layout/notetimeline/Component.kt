package com.trestudio.periodtracker.components.layout.notetimeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.viewmodel.database.NoteDB

@Composable
fun NoteList(notes: List<NoteDB>, callback: (NoteDB) -> Unit) {
    val haptics = LocalHapticFeedback.current
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn {
        items(notes) { note ->
            NoteItem(note = note, onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                callback(note)
            })
        }
    }
}

@Composable
fun NoteItem(note: NoteDB, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.extraLarge)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Date: ${note.date}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Symptoms: ${note.symptom.joinToString()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Mood: ${note.mood}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pain Level: ${note.painLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            BorderButton(onClick = onClick, text = stringResource(R.string.more))
        }
    }
}
