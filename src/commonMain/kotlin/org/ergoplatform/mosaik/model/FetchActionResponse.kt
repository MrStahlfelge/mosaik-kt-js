package org.ergoplatform.mosaik.model

import kotlinx.serialization.Serializable
import org.ergoplatform.mosaik.model.actions.Action

/**
 * Response to subsequent backend requests triggered by a
 * [org.ergoplatform.mosaik.model.actions.BackendRequestAction]
 */
class FetchActionResponse {
    var appVersion = 0
    lateinit var action: Action
}