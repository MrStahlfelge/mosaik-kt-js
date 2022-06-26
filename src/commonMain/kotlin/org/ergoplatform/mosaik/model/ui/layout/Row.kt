package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("Row")
class Row : LinearLayout() {
    override val children: MutableList<ViewElement> = ArrayList()
    private val childAlignment: MutableList<VAlignment> = ArrayList()
    private val childWeight: MutableList<Int> = ArrayList()

    var packed = false

    fun defaultChildAlignment(): VAlignment {
        return VAlignment.CENTER
    }

    override fun addChild(element: ViewElement) {
        addChild(element, defaultChildAlignment(), 0)
    }

    fun addChild(
        element: ViewElement,
        alignment: VAlignment,
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

    fun getChildAlignment(element: ViewElement): VAlignment {
        val pos = getChildPos(element)
        return childAlignment[pos]
    }

    fun getChildWeight(element: ViewElement): Int {
        val pos = getChildPos(element)
        return childWeight[pos]
    }

}