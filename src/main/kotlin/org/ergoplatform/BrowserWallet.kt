package org.ergoplatform

import kotlinx.coroutines.await
import org.ergoplatform.mosaik.MosaikLogger
import kotlin.js.Promise

// https://github.com/anon-real/ErgoAuctionHouse/blob/cba1f13e20647889a4098fca31e3a6e059ee0741/src/auction/yoroiUtils.js
// https://github.com/ergoplatform/eips/blob/b70f406ad9f3cb7088407857820660558250db73/eip-0012.md
// https://github.com/capt-nemo429/eip12-types/blob/master/src/index.ts#L99-L182

object BrowserWallet {
    fun isBrowserWalletInstalled(): Boolean = isNautilusInstalled() || isSafewInstalled()

    private fun runSafe(check: () -> Boolean): Boolean {
        return try {
            check()
        } catch (e: dynamic) {
            MosaikLogger.logDebug(e.toString())
            false
        }
    }

    fun isNautilusInstalled(): Boolean =
        runSafe { js("window.ergoConnector && !(typeof window.ergoConnector.nautilus === \"undefined\")") as Boolean }

    fun isSafewInstalled(): Boolean =
        runSafe { js("window.ergoConnector && !(typeof window.ergoConnector.safew === \"undefined\")") as Boolean }

    private suspend fun connect(alwaysReconnect: Boolean): Boolean {
        MosaikLogger.logDebug("isNautilusInstalled: ${isNautilusInstalled()}")
        MosaikLogger.logDebug("isSafewInstalled: ${isSafewInstalled()}")

        // it is enough to check for Nautilus as SAFEW is exposing the nautilus object as well
        if (!isNautilusInstalled())
            return false

        if (!alwaysReconnect) {
            try {
                if (ergoConnector.nautilus.isConnected().await()) {
                    return true
                }
            } catch (e: dynamic) {
                // isConnected() is broken for SAFEW, so this is needed
            }
        } else {
            try {
                ergoConnector.nautilus.disconnect().await()
            } catch (e: dynamic) {
                // disconnect() can throw an error
            }
        }

        return ergoConnector.nautilus.connect().await()
    }

    suspend fun getWalletAddress(alwaysReconnect: Boolean): String? {
        try {
            return if (connect(alwaysReconnect)) {
                val context = ergoConnector.nautilus.getContext().await()
                context.get_change_address().await()
            } else null
        } catch (e: dynamic) {
            MosaikLogger.logDebug(e.toString())
            throw RuntimeException("Exception $e")
        }
    }
}

private external val ergo: EIP12ErgoAPI

private typealias Address = String

private external object ergoConnector {
    val nautilus: EIP12Connection
    val safew: EIP12Connection
}

private external interface EIP12Connection {
    /**
     * Request wallet connection and instantiate a global `ergo` const with
     * the interaction API, if authorized by the user.
     */
    fun connect(): Promise<Boolean>

    /**
     * Check if the wallet is connected.
     */
    fun isConnected(): Promise<Boolean>

    /**
     * Disconnect from wallet and remove the connection in the wallet.
     */
    fun disconnect(): Promise<Boolean>

    /**
     * Get a {@link EIP12ErgoAPI} instance.
     */
    fun getContext(): Promise<EIP12ErgoAPI>
}

private external interface EIP12ErgoAPI {
    /**
     * Get user's preferred change address.
     */
    fun get_change_address(): Promise<Address>
}