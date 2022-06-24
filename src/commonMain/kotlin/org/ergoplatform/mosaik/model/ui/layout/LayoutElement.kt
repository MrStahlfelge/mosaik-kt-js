package org.ergoplatform.mosaik.model.ui.layout

import org.ergoplatform.mosaik.model.ui.ViewGroup

/**
 * ViewGroup supporting laying out the children
 */
interface LayoutElement : ViewGroup {
    var padding: Padding
}