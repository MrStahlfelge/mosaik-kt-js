package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ReloadAction")
class ReloadAction : Action {
    override lateinit var id: String
}