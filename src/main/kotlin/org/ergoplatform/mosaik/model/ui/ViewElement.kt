package org.ergoplatform.mosaik.model.ui

abstract class ViewElement : BaseAttributes {
    override var isVisible = true
    override var id: String? = null
    override var onLongPressAction: String? = null
    override var onClickAction: String? = null
}