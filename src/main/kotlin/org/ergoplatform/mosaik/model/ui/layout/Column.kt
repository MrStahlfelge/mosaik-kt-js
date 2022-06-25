package org.ergoplatform.mosaik.model.ui.layout

/**
 * Use Column to place items vertically on the screen
 */
class Column : org.ergoplatform.mosaik.model.ui.layout.LinearLayout<HAlignment>() {
    override fun defaultChildAlignment(): HAlignment {
        return HAlignment.CENTER
    }
}