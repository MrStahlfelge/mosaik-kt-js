package org.ergoplatform.mosaik.model.ui.layout

import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * Use Box to put elements on top of another
 */
open class Box : ViewElement(), LayoutElement {
    override var padding = Padding.NONE
    override val children: MutableList<ViewElement> = ArrayList()
    private val childHAlignment: MutableList<HAlignment> = ArrayList()
    private val childVAlignment: MutableList<VAlignment> = ArrayList()

    override fun addChild(element: ViewElement) {
        addChild(element, HAlignment.CENTER, VAlignment.CENTER)
    }

    fun addChild(
        element: ViewElement,
        hAlignment: HAlignment,
        vAlignment: VAlignment
    ) {
        children.add(element)
        childHAlignment.add(hAlignment)
        childVAlignment.add(vAlignment)
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

    fun getChildHAlignment(element: ViewElement): HAlignment {
        return childHAlignment[getChildPos(element)]
    }

    fun getChildVAlignment(element: ViewElement): VAlignment {
        return childVAlignment[getChildPos(element)]
    }

}