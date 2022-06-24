package org.ergoplatform.mosaik.model

/**
 * Manifest for a Mosaik app. A Mosaik app can declare properties to the Wallet application that
 * is executing it.
 */
class MosaikManifest(
    val appName: String,
    val appVersion: Int, targetMosaikVersion: Int,
    val targetCanvasDimension: CanvasDimension? = null,
    cacheLifetime: Int
) {
    var appDescription: String? = null
    var iconUrl: String? = null
    var targetMosaikVersion: Int = 0
    var cacheLifetime: Int = 0
    var errorReportUrl: String? = null

    enum class CanvasDimension {
        COMPACT_WIDTH,
        MEDIUM_WIDTH,
        FULL_WIDTH
    }
}