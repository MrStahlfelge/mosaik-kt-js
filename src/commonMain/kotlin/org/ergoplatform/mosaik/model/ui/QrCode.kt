package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * shows a QR code
 */
@Serializable
@SerialName("QrCode")
class QrCode : ViewElement() {
    var content: String? = null
}