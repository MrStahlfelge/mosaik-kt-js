package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Image")
class Image : ViewElement() {
    lateinit var url: String
    var size = Size.MEDIUM

    enum class Size {
        SMALL, MEDIUM, LARGE
    }
}