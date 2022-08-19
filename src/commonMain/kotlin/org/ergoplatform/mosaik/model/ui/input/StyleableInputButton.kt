package org.ergoplatform.mosaik.model.ui.input

interface StyleableInputButton : OptionalInputElement {
    var style: InputButtonStyle

    enum class InputButtonStyle {
        BUTTON_PRIMARY, BUTTON_SECONDARY, ICON_PRIMARY, ICON_SECONDARY
    }
}