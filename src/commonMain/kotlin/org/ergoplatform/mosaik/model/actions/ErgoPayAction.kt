package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ErgoPayAction")
class ErgoPayAction : UrlAction() {
    var onFinished: String? = null

}