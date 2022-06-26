package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LoadingIndicator")
class LoadingIndicator : ViewElement() {
    var size = Size.SMALL

    enum class Size {
        SMALL, MEDIUM
    }
}