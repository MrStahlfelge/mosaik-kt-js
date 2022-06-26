package org.ergoplatform.mosaik.model.ui.text

import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.layout.HAlignment

@Serializable
abstract class StyleableTextLabel<T> : ViewElement(), StyleableLabel, TextLabel<T> {
    override var style: LabelStyle = LabelStyle.BODY1
    override var textColor: ForegroundColor = ForegroundColor.DEFAULT
    override var maxLines = 0
    override var truncationType: TruncationType = TruncationType.END
    override var textAlignment: HAlignment = HAlignment.START
}