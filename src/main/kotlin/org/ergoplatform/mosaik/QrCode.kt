package org.ergoplatform.mosaik

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.Element

@Composable
fun QRCodeElement(
    text: String,
    qrCodeOptions: QRCodeOptions = QRCodeOptions(),
) {
    val qrCodeState = remember(text) { mutableStateOf<String?>(null) }

    LaunchedEffect(text) {
        QRCode.toDataURL(text, qrCodeOptions) { error, uri ->
            qrCodeState.value = uri
        }
    }

    qrCodeState.value?.let { qrCodeUri ->
        Img(qrCodeUri, attrs = {
            style {
                property("object-fit", "contain")
            }
        })
    }
}

@JsModule("qrcode")
@JsNonModule
external class QRCode {
    companion object {
        fun toCanvas(
            canvas: Element,
            str: String,
            options: QRCodeOptions,
            callback: (error: String?) -> Unit
        )

        fun toCanvas(canvas: Element, str: String, callback: (error: String?) -> Unit)

        fun toString(
            text: String,
            options: QRCodeOptions,
            callback: (error: String?, svg: String?) -> Unit
        )

        fun toDataURL(
            text: String,
            options: QRCodeOptions,
            callback: (error: String?, svg: String?) -> Unit
        )
    }
}

data class QRCodeOptions(
    val errorCorrectionLevel: String = "M",
    val maskPattern: Int = 0,
    val scale: Int = 4
)