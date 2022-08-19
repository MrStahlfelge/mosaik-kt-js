package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.layout.HAlignment

/**
 * shows markdown content - supported features depend on Mosaik executor
 */
@Serializable
@SerialName("MarkDown")
class MarkDown : ViewElement() {
    var content = ""
    var contentAlignment = HAlignment.START
}