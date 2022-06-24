package org.ergoplatform.mosaik.model.ui.input

import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * A text input element
 */
abstract class TextField<T : Any> : ViewElement(), InputElement<T> {
    private var endIconType: IconType? = null
    var onEndIconClicked: String? = null
    var onImeAction: String? = null
    var errorMessage: String? = null
    var placeholder: String? = null
    override var value: T? = null
    var minValue: Long = 0
    var maxValue = Long.MAX_VALUE
    override var onValueChangedAction: String? = null
    var imeActionType = ImeActionType.NEXT
    override var isEnabled = true
    var isReadOnly = false
    var endIcon: IconType? = null

    enum class ImeActionType {
        NEXT, DONE, GO, SEARCH
    }
}