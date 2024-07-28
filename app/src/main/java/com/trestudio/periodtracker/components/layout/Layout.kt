package com.trestudio.periodtracker.components.layout

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun OrientationLayout(
    verticalLayout: @Composable (@Composable () -> Unit) -> Unit,
    horizontalLayout: @Composable (@Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (portrait) {
        verticalLayout(content)
    } else {
        horizontalLayout(content)
    }
}
