package org.ergoplatform.mosaik.js

import kotlinx.browser.window

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
        val routeUrl = urlMap.get(hash.lowercase().removePrefix("#")) ?: urlMap[""]

        routeUrl?.let {
            println("Hash: $hash, current appUrl: ${runtime.appUrl}, new url: $routeUrl")
            if (runtime.appUrl != routeUrl)
                runtime.loadMosaikApp(routeUrl)
        }
    }

    fun appLoaded(appUrl: String) {
        println("appLoaded: $appUrl")
        val routeHash = urlMap.entries.firstOrNull { it.value == appUrl }?.key
        routeHash?.let {
            if (routeHash != window.location.hash)
                window.location.hash = routeHash
        }
    }
}