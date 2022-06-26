package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ErgoAuthAction")
class ErgoAuthAction : UrlAction() {
    var onFinished: String? = null

}