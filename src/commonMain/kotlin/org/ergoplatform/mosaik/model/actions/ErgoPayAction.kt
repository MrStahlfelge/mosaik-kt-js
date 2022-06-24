package org.ergoplatform.mosaik.model.actions

/**
 * Action containing an ErgoPay URL that will make the wallet app switch to ErgoPay.
 * When user navigates back, they will come back to the current view.
 */
class ErgoPayAction : UrlAction() {
    var onFinished: String? = null

}