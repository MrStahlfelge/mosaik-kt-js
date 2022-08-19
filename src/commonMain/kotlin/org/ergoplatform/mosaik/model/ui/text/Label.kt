package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Label")
open class Label : StyleableTextLabel<String>() {
    override var text: String? = null
}