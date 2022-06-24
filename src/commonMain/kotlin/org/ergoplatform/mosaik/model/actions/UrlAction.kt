package org.ergoplatform.mosaik.model.actions

abstract class UrlAction : Action {
    lateinit var url: String
    override lateinit var id: String
}