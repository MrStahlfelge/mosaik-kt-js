package org.ergoplatform.mosaik.model.ui.layout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ViewElement

@Serializable
@SerialName("Column")
class Column : LinearLayout() {
    override val children: MutableList<ViewElement> = ArrayList()
    private val childAlignment: MutableList<HAlignment> = ArrayList()
    private val childWeight: MutableList<Int> = ArrayList()

    override fun replaceChild(
        elementToReplace: ViewElement,
        newElement: ViewElement
    ) {
        children.set(getChildPos(elementToReplace), newElement)
    }

    fun getChildAlignment(element: ViewElement): HAlignment {
        val pos = getChildPos(element)
        return childAlignment[pos]
    }

    fun getChildWeight(element: ViewElement): Int {
        val pos = getChildPos(element)
        return childWeight[pos]
    }

}