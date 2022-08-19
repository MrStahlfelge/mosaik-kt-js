package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("WalletChooseButton")
class WalletChooseButton : ViewElement(), StyleableInputButton {
    var value: List<String>? = null

    @SerialName("onValueChanged")
    override var onValueChangedAction: String? = null
    override var enabled: Boolean = true
    override var mandatory: Boolean = true
    override var onClickAction: String?
        get() = null
        set(action) {}

    override var style: StyleableInputButton.InputButtonStyle =
        StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY
}