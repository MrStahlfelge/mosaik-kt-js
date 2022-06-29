package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("DropDownList")
class DropDownList : ViewElement(), OptionalInputElement {
    var value: String? = null
    @SerialName("onValueChanged")
    override var onValueChangedAction: String? = null
    override var enabled = true
    var entries: Map<String, String> = emptyMap()
    override var mandatory = true
}