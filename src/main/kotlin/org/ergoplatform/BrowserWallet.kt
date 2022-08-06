package org.ergoplatform

import kotlinx.coroutines.await
import kotlin.js.Promise

// https://github.com/anon-real/ErgoAuctionHouse/blob/cba1f13e20647889a4098fca31e3a6e059ee0741/src/auction/yoroiUtils.js
// https://github.com/ergoplatform/eips/blob/b70f406ad9f3cb7088407857820660558250db73/eip-0012.md

object BrowserWallet {
    fun isBrowserWalletInstalled(): Boolean =
        js("!(typeof ergo_request_read_access === \"undefined\")") as Boolean

    suspend fun requestReadAccess(): Boolean {
        if (!isBrowserWalletInstalled())
            return false

        val readAccess = ergo_check_read_access()

        if (readAccess)
            return true

        return ergo_request_read_access().await()
    }

    suspend fun getWalletAddress(): String? {
        try {
            return if (requestReadAccess()) {
                ergo.get_change_address().await()
            } else null
        } catch (e: dynamic) {
            throw RuntimeException("Exception $e")
        }
    }
}

private external fun ergo_check_read_access(): Boolean
private external fun ergo_request_read_access(): Promise<Boolean>

private external object ergo {
    fun get_change_address(): Promise<Address>
}

private typealias Address = String