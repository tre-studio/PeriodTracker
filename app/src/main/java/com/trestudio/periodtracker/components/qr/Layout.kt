package com.trestudio.periodtracker.components.qr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trestudio.periodtracker.R
import com.trestudio.periodtracker.components.button.BorderButton
import com.trestudio.periodtracker.components.main.BackToMainScreen
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun ShareQRcode(
    defaultLMP: MutableState<LMPstartDate?>,
    viewModel: MainViewModel
) {
    val data = defaultLMP.value
    val intro = if (data == null) {
        stringResource(R.string.this_feature_is_not_available)
    } else {
        stringResource(R.string.intro_qrcode)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) QRColumn@{
        Text(
            text = stringResource(R.string.share_qrcode),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = intro,
            style = MaterialTheme.typography.bodySmall,
        )
        if (data == null) return@QRColumn

        val qrData = Json.encodeToString(data)
        val qrBitmap = generateQRCode(qrData, MaterialTheme.colorScheme.onPrimaryContainer)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            QRCodeScreen(qrBitmap)
        }

        Text(
            text = stringResource(R.string.or_shareqr),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.scan_qrcode),
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            BorderButton(onClick = {
                viewModel.setMainScreenState(MainScreenState.QRcodeCamera)
                viewModel.setSettingButtonState(SettingButtonState.ReturnButton)
            }, text = stringResource(R.string.open_camera))
        }
    }

    BackToMainScreen(viewModel)
}