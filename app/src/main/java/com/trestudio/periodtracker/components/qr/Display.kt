package com.trestudio.periodtracker.components.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun QRCodeScreen(qrCodeBitmap: Bitmap) {
    Image(bitmap = qrCodeBitmap.asImageBitmap(), contentDescription = "QR Code")
}
