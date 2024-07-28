package com.trestudio.periodtracker.components.layout.titlebar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun TransitionName(
    list: List<String>,
    inBetweenTime: Long = 1000,
    transitionDuration: Long = 250,
) {
    if (list.isEmpty()) return

    val visible = remember { mutableStateOf(true) }
    val index = remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(index.intValue) {
        delay(inBetweenTime)
        visible.value = false
        delay(transitionDuration)
        index.intValue = (index.intValue + 1) % list.size
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Text(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            text = "${list[index.intValue]}."
        )
    }
}