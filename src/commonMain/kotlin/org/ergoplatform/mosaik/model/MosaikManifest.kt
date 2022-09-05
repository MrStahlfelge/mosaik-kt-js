package org.ergoplatform.mosaik.model

import kotlinx.serialization.Serializable

/**
 * Manifest for a Mosaik app. A Mosaik app can declare properties to the Wallet application that
 * is executing it.
 */
@Serializable
class MosaikManifest(
    val appName: String,
    val appVersion: Int,
    val targetMosaikVersion: Int,
    val targetCanvasDimension: CanvasDimension? = null,
    val cacheLifetime: Int
) {
    var appDescription: String? = null
    var iconUrl: String? = null
    var errorReportUrl: String? = null
    var onAppLoadedAction: String? = null
    var onResizeAction: String? = null

    enum class CanvasDimension {
        COMPACT_WIDTH,
        MEDIUM_WIDTH,
        FULL_WIDTH
    }
}