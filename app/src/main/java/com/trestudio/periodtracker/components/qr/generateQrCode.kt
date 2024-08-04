package com.trestudio.periodtracker.components.qr

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

const val size = 512 // QR code image size
fun generateQRCode(text: String, color: Color): Bitmap {
    val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val primaryColor = color.toArgb()
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bits[x, y]) primaryColor else android.graphics.Color.TRANSPARENT)
        }
    }
    return bitmap
}