package com.trestudio.periodtracker.components.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.theme.Theme
import com.trestudio.periodtracker.viewmodel.MainViewModel

// note: add .safeDrawingPadding() on modifier

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainLayout(viewModel: MainViewModel) {
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val doneGettingStarted = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        doneGettingStarted.value = viewModel.getIntroStatus() ?: false
        isLoading = false
    }

    Theme.Setup {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                } else {
                    if (!doneGettingStarted.value) {
                        GettingStartedLayout(coroutineScope, doneGettingStarted, viewModel)
                    } else {
                        MainApplicationLayout(portrait, viewModel, coroutineScope)
                    }
                }
            }
        }
    }
}
