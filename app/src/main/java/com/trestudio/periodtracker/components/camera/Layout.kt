package com.trestudio.periodtracker.components.camera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.trestudio.periodtracker.viewmodel.MainViewModel
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun ScanCamera(
    permissionRequest: ActivityResultLauncher<String>,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel,
    defaultLMP: MutableState<LMPstartDate?>
) {
    var cameraPermission by remember {
        mutableStateOf(false)
    }

    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.CAMERA
        ) -> {
            cameraPermission = true
        }

        else -> {
            permissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Scan QR",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(32.dp))
        if (cameraPermission) {
            CameraPreview(modifier = Modifier.weight(1f)) { barcode ->
                barcode.displayValue?.let {
                    try {
                        val result = Json.decodeFromString<LMPstartDate>(it)
                        coroutineScope.launch {
                            viewModel.completeLMPstartDate(
                                result.value,
                                result.avgCycle.toUInt(),
                                result.avgPeriod.toUInt()
                            )
                            viewModel.setMainScreenState(MainScreenState.MainApp)
                            viewModel.setSettingButtonState(SettingButtonState.SettingButton)
                            defaultLMP.value = viewModel.getLMPstartDate()
                        }
                    } catch (e: Exception) {
                        Log.e("Test", "Error: $e")
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        viewModel.setMainScreenState(MainScreenState.QRcode)
        viewModel.setSettingButtonState(SettingButtonState.SettingButton)
    }
}
