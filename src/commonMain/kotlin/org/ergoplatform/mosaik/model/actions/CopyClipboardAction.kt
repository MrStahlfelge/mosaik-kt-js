package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CopyClipboardAction")
class CopyClipboardAction : Action {
    lateinit var text: String

    override lateinit var id: String
}