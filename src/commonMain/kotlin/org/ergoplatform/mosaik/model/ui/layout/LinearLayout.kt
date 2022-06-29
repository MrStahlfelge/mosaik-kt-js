package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * LinearLayout places items stacked on the screen
 */
@Serializable
abstract class LinearLayout : ViewElement(), LayoutElement {
    fun getChildPos(element: ViewElement): Int {
        for (i in children.indices) {
            if (children[i] === element) return i
        }
        return -1
    }
}