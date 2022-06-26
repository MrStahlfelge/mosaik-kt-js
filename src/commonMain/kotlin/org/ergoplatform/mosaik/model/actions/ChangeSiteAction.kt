package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.ViewContent

@Serializable
@SerialName("ChangeSiteAction")
open class ChangeSiteAction : Action {
    lateinit var newContent: ViewContent

    override lateinit var id: String

}