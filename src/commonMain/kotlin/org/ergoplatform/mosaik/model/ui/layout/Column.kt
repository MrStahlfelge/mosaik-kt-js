package org.ergoplatform.mosaik.model.ui.layout

/**
 * Use Column to place items vertically on the screen
 */
class Column : LinearLayout<HAlignment>() {
    override fun defaultChildAlignment(): HAlignment {
        return HAlignment.CENTER
    }
}