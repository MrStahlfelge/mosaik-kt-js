package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("DialogAction")
class DialogAction : Action {
    lateinit var message: String
    var positiveButtonText: String? = null
    var negativeButtonText: String? = null
    var onPositiveButtonClicked: String? = null
    var onNegativeButtonClicked: String? = null
    override lateinit var id: String
}