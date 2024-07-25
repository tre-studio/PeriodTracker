package com.trestudio.periodtracker.components.main

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.theme.Theme
import com.trestudio.periodtracker.viewmodel.MainViewModel

// note: add .safeDrawingPadding() on modifier

@Composable
fun MainLayout(viewModel: MainViewModel) {
    val configuration = LocalConfiguration.current
    Theme.Setup {
        Surface(
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
        ) {
//          Todo: First time opening + configuration.

            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    HorizontalLayout()
                }
                Configuration.ORIENTATION_PORTRAIT -> {
                    VerticalLayout()
                }
                else -> {}
            }
        }
    }
}

@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun MainLayoutDarkPreview() {
    MainLayout(MainViewModel())
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MainLayoutPreview() {
    MainLayout(MainViewModel())
}