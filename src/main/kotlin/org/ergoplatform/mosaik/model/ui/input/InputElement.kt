package org.ergoplatform.mosaik.model.ui.input

interface InputElement<T : Any> {
    var value: T?

    var onValueChangedAction: String?
    var isEnabled: Boolean
}