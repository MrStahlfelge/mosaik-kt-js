package org.ergoplatform.mosaik.model.ui.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ui.IconType
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * A text input element
 */
@Serializable
abstract class TextField<T : Any> : ViewElement(), InputElement<T> {
    private var endIconType: IconType? = null
    var onEndIconClicked: String? = null
    var onImeAction: String? = null
    var errorMessage: String? = null
    var placeholder: String? = null
    override var value: T? = null
    var minValue: Long = 0
    var maxValue = Long.MAX_VALUE
    @SerialName("onValueChanged")
    override var onValueChangedAction: String? = null
    var imeActionType = ImeActionType.NEXT
    override var enabled = true
    var readOnly = false
    var endIcon: IconType? = null

    enum class ImeActionType {
        NEXT, DONE, GO, SEARCH
    }
}