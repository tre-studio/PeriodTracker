package com.trestudio.periodtracker.components.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.components.layout.CalendarLayout
import com.trestudio.periodtracker.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class State {
    Intro, Input,
}

@Composable
fun GettingStartedLayout(
    coroutineScope: CoroutineScope, doneGettingStarted: MutableState<Boolean>, viewModel: MainViewModel
) {
    val state = rememberSaveable { mutableStateOf(State.Intro) }
    val scrollState = rememberScrollState()
    val scrollbarColor = MaterialTheme.colorScheme.primary
    val LMPstate: MutableState<LocalDate?> = remember {
        mutableStateOf(null)
    }
    val avgycleLengthState = remember {
        mutableStateOf(TextFieldValue())
    }
    val avgPeriodLengthState = remember {
        mutableStateOf(TextFieldValue())
    }
    val errorState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .safeDrawingPadding()
            .pointerInput(Unit) {},
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier
                .weight(4f)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = state.value == State.Intro,
                enter = fadeIn() + slideInVertically { fullHeight -> fullHeight },
                exit = fadeOut() + slideOutVertically { fullHeight -> -fullHeight }) {
                Column(
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.weight(2f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "Start Your Journey to Better Understanding Your Cycle.",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                    Row {
                        Icon(painter = painterResource(R.drawable.arrow_right_alt_48px), contentDescription = null)
                        Text(
                            text = "Welcome to period tracker app, your partner in tracking and understanding your " + "menstrual cycle. Our app provides a simple, intuitive way to monitor your symptoms, " + "predict important days, and gain insights into your health. Begin by setting up " + "your cycle details, and let us help you stay informed and in control.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            androidx.compose.animation.AnimatedVisibility(visible = state.value == State.Input,
                enter = fadeIn() + slideInVertically { fullHeight -> fullHeight },
                exit = fadeOut() + slideOutVertically { fullHeight -> -fullHeight }) {
                Column(verticalArrangement = Arrangement.spacedBy(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .drawWithContent {
                            drawContent()
                            val height = size.height
                            val scrollbarY =
                                size.height * (scrollState.value.toFloat() / scrollState.maxValue.toFloat())
                            drawRect(
                                color = scrollbarColor,
                                topLeft = Offset(size.width - 2.dp.toPx(), scrollbarY),
                                size = Size(2.dp.toPx(), height)
                            )

                        }) {
                    Column2Elements(content = {
                        Column {
                            Text("Last Menstrual Period (LMP) Start Date")
                            Spacer(Modifier.height(8.dp))
                            Text(LMPstate.value?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "")
                        }
                    }, content2 = {
                        CalendarLayout(
                            10, 0, viewModel = viewModel
                        ) { value, _ ->
                            LMPstate.value = value.date
                        }
                    })

                    Column2Elements(content = {
                        Column {
                            Text("Average Cycle Length (vary from 21 to 35 days)")
                            Spacer(Modifier.height(8.dp))
                            Text(avgycleLengthState.value.text)
                        }
                    }, content2 = {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = avgycleLengthState.value,
                            onValueChange = { newValue ->
                                val filteredValue = newValue.text.filter { it.isDigit() }
                                avgycleLengthState.value = TextFieldValue(
                                    filteredValue, selection = newValue.selection
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    })

                    Column2Elements(content = {
                        Column {
                            Text("Average Period Length (vary from 2 to 7 days)")
                            Spacer(Modifier.height(8.dp))
                            Text(avgPeriodLengthState.value.text)
                        }
                    }, content2 = {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = avgPeriodLengthState.value,
                            onValueChange = { newValue ->
                                val filteredValue = newValue.text.filter { it.isDigit() }
                                avgPeriodLengthState.value =
                                    TextFieldValue(filteredValue, selection = newValue.selection)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    })
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            if (state.value == State.Intro) {
                BorderButton(onClick = { state.value = State.Input }, text = "Next")
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
                ) {
                    BorderButton(onClick = { state.value = State.Intro }, text = "Back")
                    BorderButton(onClick = {
                        if (LMPstate.value != null
                            && avgycleLengthState.value.text.isNotEmpty()
                            && avgPeriodLengthState.value.text.isNotEmpty()
                        ) {
                            coroutineScope.launch {
                                viewModel.completeLMPstartDate(
                                    LMPstate.value!!,
                                    avgycleLengthState.value.text.toUInt(),
                                    avgPeriodLengthState.value.text.toUInt()
                                )
                                doneGettingStarted.value = viewModel.completeIntro()
                            }
                        } else {
                            errorState.value = true
                        }
                    }, text = "Submit")
                }
            }
        }
    }

    if (errorState.value) {
        AlertDialog(
            onDismissRequest = { errorState.value = false },
            title = { Text("Hey you...") },
            text = { Text("There are some missing fields, please make sure to complete them.") },
            confirmButton = {
                Button(onClick = { errorState.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun Column2Elements(
    content: @Composable () -> Unit, content2: @Composable () -> Unit
) {
    Column(
        modifier = Modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        content()
        content2()
    }
}
