package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FiatAmountLabel")
class FiatAmountLabel : StyleableTextLabel<Long>() {
    var fallbackToErg = false
    override var text: Long? = null
}