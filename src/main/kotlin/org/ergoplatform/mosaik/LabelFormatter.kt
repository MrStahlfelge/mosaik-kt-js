package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel

object LabelFormatter {

    /**
     * returns formatted text to show to the user. If null is returned, no element should be
     * shown at all
     */
    fun getFormattedText(element: StyleableTextLabel<*>, treeElement: TreeElement): String {
        return when (element) {

            is Label -> element.text ?: ""

            else -> element.text.toString()
        }
    }
}