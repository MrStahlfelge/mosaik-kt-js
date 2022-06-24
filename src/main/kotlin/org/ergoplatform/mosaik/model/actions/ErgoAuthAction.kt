package org.ergoplatform.mosaik.model.actions

/**
 * Action containing an ErgoAuth URL that will make the wallet app switch to ErgoAuth.
 * When user navigates back, they will come back to the current view.
 */
class ErgoAuthAction : UrlAction() {
    var onFinished: String? = null

}