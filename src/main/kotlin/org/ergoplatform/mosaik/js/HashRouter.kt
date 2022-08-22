package org.ergoplatform.mosaik.js

import kotlinx.browser.window
import org.ergoplatform.mosaik.MosaikLogger

const val paramDelimiter = ';'

class HashRouter(
    private val runtime: JsMosaikRuntime,
) {
    private var urlMap: MutableMap<String, String> = emptyMap<String, String>().toMutableMap()

    fun setConfig(config: MosaikConfiguration) {
        urlMap.clear()
        config.routes?.let { urlMap.putAll(it) }
        urlMap[""] = config.starturl
    }

    fun hashChanged(hash: String) {
        val hashWithoutParams = hash.lowercase().removePrefix("#").substringBefore(paramDelimiter)
        val params = hash.substringAfter(paramDelimiter, "")
            .let { "?" + it.replace(paramDelimiter, '&') }

        val routeUrl = (urlMap.get(hashWithoutParams) ?: urlMap[""])?.let { it + params }

        routeUrl?.let {
            MosaikLogger.logDebug("Hash: $hash, current appUrl: ${runtime.appUrl}, new url: $routeUrl")
            if (runtime.appUrl != routeUrl)
                runtime.loadMosaikApp(routeUrl)
        }
    }

    fun appLoaded(appUrl: String) {
        MosaikLogger.logDebug("appLoaded: $appUrl")

        val appUrlWithoutParams = appUrl.substringBefore('?')
        val params = appUrl.substringAfter('?', "").let {
            if (it.isNotBlank())
                paramDelimiter + it.replace('&', paramDelimiter)
            else ""
        }

        val routeHash = urlMap.entries.firstOrNull { it.value == appUrlWithoutParams }?.key?.let {
            it + params
        }
        routeHash?.let {
            if (routeHash != window.location.hash)
                window.location.hash = routeHash
        }
    }
}