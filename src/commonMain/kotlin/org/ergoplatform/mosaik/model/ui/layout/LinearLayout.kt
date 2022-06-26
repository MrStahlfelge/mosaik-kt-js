package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * LinearLayout places items stacked on the screen
 */
abstract class LinearLayout : ViewElement(), LayoutElement {
    override var padding = Padding.NONE

    fun getChildPos(element: ViewElement): Int {
        for (i in 0 until children.size) {
            if (children[i] === element) return i
        }
        return -1
    }
}