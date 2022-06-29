package org.ergoplatform.mosaik.model.ui.input

interface InputElement {
    // var value: Any?

    var onValueChangedAction: String?
    var enabled: Boolean
}