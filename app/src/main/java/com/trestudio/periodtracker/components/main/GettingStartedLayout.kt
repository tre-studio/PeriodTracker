package com.trestudio.periodtracker.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.trestudio.periodtracker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private enum class State {
    Intro,
    Input,
}

@Composable
fun GettingStartedLayout(
    coroutineScope: CoroutineScope,
    doneGettingStarted: MutableState<Boolean>,
    viewModel: MainViewModel
) {
    val state = rememberSaveable { mutableStateOf(State.Intro) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .safeDrawingPadding()
            .pointerInput(Unit) {},

        verticalArrangement = Arrangement.Bottom,
    ) {
        when (state.value) {
            State.Intro -> {
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxSize()
                ) {

                }
            }

            State.Input -> {
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxSize()
                ) {

                }
            }
        }
    }
}
