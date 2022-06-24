package org.ergoplatform.mosaik.model.actions

/**
 * Will open detailed information about a token
 */
class TokenInformationAction(var tokenId: String) : Action {
    override lateinit var id: String
}