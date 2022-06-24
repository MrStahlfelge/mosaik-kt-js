package org.ergoplatform.mosaik.model.ui.text

import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.layout.HAlignment

/**
 * Shows text
 */
open class StyleableTextLabel<T> : ViewElement(), StyleableLabel, TextLabel<T> {
    override var style: LabelStyle = LabelStyle.BODY1
    override var textColor: ForegroundColor = ForegroundColor.DEFAULT
    override var text: T? = null
    override var maxLines = 0
    override var truncationType: TruncationType = TruncationType.END
    override var textAlignment: HAlignment = HAlignment.START
}