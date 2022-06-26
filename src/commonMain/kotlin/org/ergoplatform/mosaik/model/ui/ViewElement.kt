package org.ergoplatform.mosaik.model.ui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class ViewElement : BaseAttributes {
    override var visible = true
    override var id: String? = null
    @SerialName("onLongPress")
    override var onLongPressAction: String? = null
    @SerialName("onClick")
    override var onClickAction: String? = null
}