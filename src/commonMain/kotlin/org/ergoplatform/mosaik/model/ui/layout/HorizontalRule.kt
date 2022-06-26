package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("HorizontalRule")
class HorizontalRule : ViewElement() {
    var vPadding = Padding.DEFAULT
}