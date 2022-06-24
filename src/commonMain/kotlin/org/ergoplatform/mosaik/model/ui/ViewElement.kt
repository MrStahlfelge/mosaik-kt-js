package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.Serializable

@Serializable
abstract class ViewElement : BaseAttributes {
    override var isVisible = true
    override var id: String? = null
    override var onLongPressAction: String? = null
    override var onClickAction: String? = null
}