package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * Grid is a responsive element presenting child elements in a grid-style layout. The number of
 * columns is determined by the min element width.
 */

@Serializable
@SerialName("Grid")
class Grid : ViewElement(), LayoutElement {
    override var padding = Padding.NONE
    override val children: MutableList<ViewElement> = ArrayList()

    @SerialName("size")
    var elementSize = ElementSize.SMALL

    fun addChild(element: ViewElement) {
        children.add(element)
    }

    override fun replaceChild(
        elementToReplace: ViewElement,
        newElement: ViewElement
    ) {
        children[getChildPos(elementToReplace)] = newElement
    }

    private fun getChildPos(element: ViewElement): Int {
        for (i in 0 until children.size) {
            if (children[i] === element) return i
        }
        return -1
    }

    enum class ElementSize {
        /**
         * Might display a two-column layout on bigger phones
         */
        MIN,

        /**
         * Will display a one-column layout on nearly all phones
         */
        SMALL,
        MEDIUM,
        LARGE
    }
}