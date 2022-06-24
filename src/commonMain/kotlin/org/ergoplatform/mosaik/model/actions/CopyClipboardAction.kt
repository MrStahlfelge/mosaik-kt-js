package org.ergoplatform.mosaik.model.actions

/**
 * Copies text into the system clipboard
 */
class CopyClipboardAction : Action {
    lateinit var text: String

    override lateinit var id: String
}