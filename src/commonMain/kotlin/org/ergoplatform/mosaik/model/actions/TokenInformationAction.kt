package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TokenInformationAction")
class TokenInformationAction(var tokenId: String) : Action {
    override lateinit var id: String
}