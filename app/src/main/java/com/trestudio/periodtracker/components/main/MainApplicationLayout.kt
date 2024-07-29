package com.trestudio.periodtracker.components.main

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.components.layout.CalendarLayout
import com.trestudio.periodtracker.components.layout.HorizontalLayout
import com.trestudio.periodtracker.components.layout.OrientationLayout
import com.trestudio.periodtracker.components.layout.VerticalLayout
import com.trestudio.periodtracker.components.layout.titlebar.TitleBar
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainApplicationLayout(portrait: Boolean, viewModel: MainViewModel, coroutineScope: CoroutineScope) {
    val defaultLMP: MutableState<LMPstartDate?> = remember {
        mutableStateOf(null)
    }

    LaunchedEffect(true) {
        defaultLMP.value = viewModel.getLMPstartDate()
        Log.i("Test", defaultLMP.value.toString())
    }

    if (defaultLMP.value == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        TitleBar(portrait)
        OrientationLayout(
            verticalLayout = { VerticalLayout(it) },
            horizontalLayout = { HorizontalLayout(it) }
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            ) {
                BorderButton(onClick = {
                    coroutineScope.launch {
                        Log.i("Test", viewModel.getLMPstartDate().toString())
                    }
                }, text = "Click here")
            }

            Box(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize()
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollableState { delta -> delta}
                    )
            ) {
                CalendarLayout(10, 10, listOf()) {}
            }
        }
    }
}