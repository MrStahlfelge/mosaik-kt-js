package org.ergoplatform.mosaik.model

/**
 * With a MosaikContext a Wallet application executing a Mosaik app can declare properties the
 * app is executed with.
 */
class MosaikContext(
    val mosaikVersion: Int, val guid: String, val language: String,
    val walletAppName: String,
    val walletAppVersion: String,
    val walletAppPlatform: Platform
) {
    enum class Platform {
        DESKTOP, TABLET, PHONE
    }

    companion object {
        const val LIBRARY_MOSAIK_VERSION = 0
    }

}