package org.ergoplatform.mosaik.model.ui

interface BaseAttributes {
    var isVisible: Boolean

    val id: String?

    var onLongPressAction: String?

    var onClickAction: String?
}