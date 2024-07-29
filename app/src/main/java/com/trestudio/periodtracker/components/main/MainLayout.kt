package com.trestudio.periodtracker.components.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.components.theme.Theme
import com.trestudio.periodtracker.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// note: add .safeDrawingPadding() on modifier

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainLayout(viewModel: MainViewModel) {
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val doneGettingStarted = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        doneGettingStarted.value = viewModel.getIntroStatus() ?: false
    }

    Theme.Setup {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (!doneGettingStarted.value) {
                GettingStartedLayout(coroutineScope, doneGettingStarted, viewModel)
            } else {
                MainApplicationLayout(portrait, viewModel, coroutineScope)
            }
        }
    }
}
