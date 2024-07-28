package com.trestudio.periodtracker.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.OrientationLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.layout.titlebar.TitleBar

@Composable
fun MainApplicationLayout(portrait: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        TitleBar(portrait)
        OrientationLayout(
            verticalLayout = { VerticalLayout(it) },
            horizontalLayout = { HorizontalLayout(it) },
            content = {
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                ) {
                    Text(text = "Hello world")
                }

                Box(
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxSize()
                ) {
//
                }
            }
        )
    }
}