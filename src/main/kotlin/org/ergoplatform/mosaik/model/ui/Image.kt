package org.ergoplatform.mosaik.model.ui

/**
 * Shows an image
 */
class Image : ViewElement() {
    lateinit var url: String
    var size = Size.MEDIUM

    enum class Size {
        SMALL, MEDIUM, LARGE
    }
}