package org.ergoplatform.mosaik.model

import kotlinx.serialization.Serializable

/**
 * With a MosaikContext a Wallet application executing a Mosaik app can declare properties the
 * app is executed with.
 */
@Serializable
class MosaikContext(
    val mosaikVersion: Int,
    val guid: String,
    val language: String,
    val walletAppName: String,
    val walletAppVersion: String,
    val walletAppPlatform: Platform,
    val timeZone: Int,
) {
    enum class Platform {
        DESKTOP, TABLET, PHONE
    }

    companion object {
        const val LIBRARY_MOSAIK_VERSION = 2
        const val EXECUTOR_VERSION = "2.0.0"
    }

}