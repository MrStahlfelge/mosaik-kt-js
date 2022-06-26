package org.ergoplatform.mosaik.model.actions

import kotlinx.serialization.Serializable

@Serializable
abstract class UrlAction : Action {
    lateinit var url: String
    override lateinit var id: String
}