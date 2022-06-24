package org.ergoplatform.mosaik.model.ui.text

import org.ergoplatform.mosaik.model.ui.ForegroundColor

interface StyleableLabel {
    var style: LabelStyle
    var textColor: ForegroundColor
}