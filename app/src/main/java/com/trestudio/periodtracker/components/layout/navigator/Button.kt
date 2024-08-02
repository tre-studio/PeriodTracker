package com.trestudio.periodtracker.components.layout.navigator

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun NavButton(
    painterID: Int,
    painterDescriptionID: Int,
    textID: Int,
    visible: Boolean,
    modifier: Modifier,
    callback: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                callback()
            },
    ) {
        Row(
            modifier = Modifier
                .border(
                    1.dp,
                    if (visible) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        Color.Transparent
                    },
                    CircleShape
                )
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(painterID),
                contentDescription = stringResource(painterDescriptionID),
            )
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        style = MaterialTheme.typography.titleSmall,
                        text = stringResource(textID),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}