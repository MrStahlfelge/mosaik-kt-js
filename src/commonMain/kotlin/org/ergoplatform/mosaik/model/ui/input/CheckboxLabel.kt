package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.text.Label

/**
 * Label showing a checkbox/switch. A value of null means the box is neither checked nor not checked
 */
@Serializable
@SerialName("CheckboxLabel")
class CheckboxLabel : Label(), OptionalInputElement {
    var value: Boolean? = null

    @SerialName("onValueChanged")
    override var onValueChangedAction: String? = null
    override var enabled = true
    override var mandatory = true
}