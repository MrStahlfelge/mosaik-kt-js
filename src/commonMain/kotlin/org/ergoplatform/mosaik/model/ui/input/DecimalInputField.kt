package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DecimalInputField")
class DecimalInputField : LongTextField() {
    var scale = 9
}