package org.ergoplatform.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ui.layout.Box

object MosaikSerializers {
    fun parseMosaikAppFromJson(json: String): MosaikApp {
        val jsonObject = Json.parseToJsonElement(json).jsonObject

        // TODO parse view content and actions
        val view = Box()

        return MosaikApp(view).apply {
            manifest = Json.decodeFromJsonElement<MosaikManifest>(jsonObject["manifest"]!!)
        }
    }
}