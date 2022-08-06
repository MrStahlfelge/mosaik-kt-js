package org.ergoplatform.mosaik.js

import kotlinx.serialization.Serializable

@Serializable
data class MosaikConfiguration(
    /**
     * url of the Mosaik app the executor starts with
     */
    var starturl: String,
    /**
     * if wanted, different Mosaik apps can be mapped to a hash route in the browser window.
     * This enables the ability to use the browser back/forward navigation buttons.
     *
     * The map keys are the hash routes, the values the addresses of the Mosaik apps that are mapped
     */
    var routes: Map<String, String>? = null,
    /**
     * if set to yes, users can paste wallet addresses manually for connecting their wallet
     */
    var chooseWalletManually: Boolean = true,
    /**
     * if set to yes, users can use a browser extension wallet for connecting their wallet,
     */
    val chooseWalletWithExtension: Boolean = true,
    /**
     * if set, users can use ErgoPay to connect their wallet
     */
    val chooseWalletErgoPay: ConnectWalletErgoPayConfig? = null,
)

@Serializable
data class ConnectWalletErgoPayConfig(
    /**
     * URL the Mosaik UI sends GET requests to in order to check if user connected his wallet
     * The requests are send every few seconds while the Connect wallet dialog is opened and no
     * wallet address was connected yet. Placeholder %GUID% is needed.
     *
     * Expected response body is a String with the wallet address, or null.
     */
    var getAddressForSessionUrl: String,
    /**
     * ErgoPay URL the wallet connects to in order for the user to connect his wallet
     * Placeholder %GUID% and %P2PK_ADDRESS% are needed.
     *
     * Expected response body is an ErgoPay response URL.
     */
    var ergoPaySetAddressUrl: String,
)