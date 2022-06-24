package org.ergoplatform.mosaik.model.ui.input

interface OptionalInputElement<T : Any> : InputElement<T> {
    var isMandatory: Boolean
}