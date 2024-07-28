package com.trestudio.periodtracker.components.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.components.theme.Theme
import com.trestudio.periodtracker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// note: add .safeDrawingPadding() on modifier

@Composable
fun MainLayout(viewModel: MainViewModel) {
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val doneGettingStarted = remember { mutableStateOf(false) }
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
            MainApplicationLayout(portrait)
            if (!doneGettingStarted.value) {
                GettingStartedLayout(coroutineScope, doneGettingStarted, viewModel)
            }
        }
    }
}
