package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BackendRequestAction")
class BackendRequestAction : UrlAction() {
    var postValues = PostValueType.ALL

    enum class PostValueType {
        ALL,
        VALID,
        NONE
    }
}