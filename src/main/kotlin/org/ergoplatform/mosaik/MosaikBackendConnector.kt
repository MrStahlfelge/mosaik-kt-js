package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.ViewContent

/**
 * Handles connection from Mosaik executor app its backend, the dApp
 */
interface MosaikBackendConnector {
    /**
     * first load of a Mosaik app. Blocking, call on a background thread
     * For connection related problems, [ConnectionException] should be thrown
     */
    suspend fun loadMosaikApp(
        url: String,
        referrer: String?,
    ): AppLoaded

    /**
     * loads an action from Mosaik app. Blocking, call on a background thread
     * For connection related problems, [ConnectionException] should be thrown
     */
    suspend fun fetchAction(
        url: String, baseUrl: String?,
        values: Map<String, Any?>,
        referrer: String?,
    ): FetchActionResponse

    /**
     * loads contents of a LazyLoadBox. Blocking, call on a background thread
     */
    suspend fun fetchLazyContent(
        url: String,
        baseUrl: String?,
        referrer: String
    ): ViewContent

    /**
     * reports an error to the given url
     */
    suspend fun reportError(reportUrl: String, appUrl: String, t: Throwable)

    data class AppLoaded(val mosaikApp: MosaikApp, val appUrl: String)
}