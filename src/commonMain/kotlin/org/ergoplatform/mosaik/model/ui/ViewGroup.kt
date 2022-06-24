package org.ergoplatform.mosaik.model.ui

/**
 * A view group can contain many child view elements
 */
interface ViewGroup {
    /**
     * @return list of child view elements
     */
    val children: List<ViewElement>

    fun addChild(element: ViewElement)

    fun replaceChild(elementToReplace: ViewElement, newElement: ViewElement)
}