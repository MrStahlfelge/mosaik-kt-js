package org.ergoplatform.mosaik.model.actions

/**
 * Shows an error or info dialog that is shown modal on top of the current view.
 */
class DialogAction : Action {
    lateinit var message: String
    var positiveButtonText: String? = null
    var negativeButtonText: String? = null
    var onPositiveButtonClicked: String? = null
    var onNegativeButtonClicked: String? = null
    override lateinit var id: String
}