package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Icon")
class Icon : ViewElement() {
    @SerialName("icon")
    var iconType = IconType.INFO
    @SerialName("size")
    var iconSize = Size.SMALL
    var tintColor = ForegroundColor.DEFAULT
    enum class Size {
        SMALL, MEDIUM, LARGE
    }
}