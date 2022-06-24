package org.ergoplatform.mosaik.model.ui.layout

import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * LinearLayout places items stacked on the screen
 */
abstract class LinearLayout<CSA> : ViewElement(), LayoutElement {
    override var padding = Padding.NONE

    override val children: MutableList<ViewElement> = ArrayList()
    private val childAlignment: MutableList<CSA> = ArrayList()
    private val childWeight: MutableList<Int> = ArrayList()

    override fun addChild(element: ViewElement) {
        addChild(element, defaultChildAlignment(), 0)
    }

    abstract fun defaultChildAlignment(): CSA
    fun addChild(
        element: ViewElement,
        alignment: CSA,
        childWeight: Int
    ) {
        children.add(element)
        childAlignment.add(alignment)
        this.childWeight.add(childWeight)
    }

    override fun replaceChild(
        elementToReplace: ViewElement,
        newElement: ViewElement
    ) {
        children.set(getChildPos(elementToReplace), newElement)
    }

    private fun getChildPos(element: ViewElement): Int {
        for (i in 0 until children.size) {
            if (children[i] === element) return i
        }
        return -1
    }

    fun getChildAlignment(element: ViewElement): CSA {
        val pos = getChildPos(element)
        return childAlignment[pos]
    }

    fun getChildWeight(element: ViewElement): Int {
        val pos = getChildPos(element)
        return childWeight[pos]
    }

}