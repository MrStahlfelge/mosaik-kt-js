package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("ErgoAddressChooseButton")
class ErgoAddressChooseButton : ViewElement(), OptionalInputElement<String> {
    override var value: String? = null
    @SerialName("onValueChanged")
    override var onValueChangedAction: String? = null
    override var enabled = true
    override var mandatory = true
    override var onClickAction: String?
        get() = null
        set(action) {}
}