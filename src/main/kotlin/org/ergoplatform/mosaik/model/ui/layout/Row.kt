package org.ergoplatform.mosaik.model.ui.layout

/**
 * Use Row to place items horizontally on the screen.
 * By default, Row will fill all available horizontal space. If that is not wanted, set it's
 * `packed` property to `true`
 */
class Row : LinearLayout<VAlignment?>() {
    var isPacked = false

    override fun defaultChildAlignment(): VAlignment {
        return VAlignment.CENTER
    }

}