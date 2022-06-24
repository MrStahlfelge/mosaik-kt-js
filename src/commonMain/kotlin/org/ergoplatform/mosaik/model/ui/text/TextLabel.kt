package org.ergoplatform.mosaik.model.ui.text

import org.ergoplatform.mosaik.model.ui.layout.HAlignment

interface TextLabel<T> : TextElement<T> {
    /**
     * @return max lines to show, with 0 showing indefinite lines
     */
    var maxLines: Int

    var truncationType: TruncationType

    var textAlignment: HAlignment
}