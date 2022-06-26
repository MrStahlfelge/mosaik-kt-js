package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ErgAmountLabel")
class ErgAmountLabel : StyleableTextLabel<Long>() {
    var withCurrencySymbol = true
    var maxDecimals = 4
    var trimTrailingZero = false
    override var text: Long? = null
}