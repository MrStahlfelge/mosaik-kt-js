package org.ergoplatform.mosaik.model.ui

interface BaseAttributes {
    var visible: Boolean

    val id: String?

    var onLongPressAction: String?

    var onClickAction: String?
}