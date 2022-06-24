package org.ergoplatform.mosaik.model

import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ui.ViewElement

/**
 * On first request, the backend will respond with ViewContent completed by a manifest
 */
class MosaikApp(view: ViewElement) : ViewContent(view) {
    lateinit var manifest: MosaikManifest
}