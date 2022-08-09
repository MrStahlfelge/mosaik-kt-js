package org.ergoplatform.mosaik.js

import org.ergoplatform.mosaik.model.MosaikApp
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.actions.ReloadAction
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.Label
import org.ergoplatform.mosaik.model.ui.text.LabelStyle

/**
 * Constructs a not loaded error screen with retry button
 */
object NotLoadedApp {
    fun getApp(url: String): MosaikApp {
        val rootView = Box().apply {
            padding = Padding.DEFAULT
        }

        val errorMsg = Label().apply {
            text = "Could not load application.\nCheck your connection and try again."
            style = LabelStyle.HEADLINE2
            textAlignment = HAlignment.CENTER
        }

        val reloadActionId = "reload"
        val retryButton = Button().apply {
            text = "Retry"
            onClickAction = reloadActionId
        }

        val verticalLayout = Column().apply {
            listOf(errorMsg, Box().apply {
                padding = Padding.DEFAULT
            }, retryButton).forEach { addChild(it, HAlignment.CENTER, 0) }
        }

        rootView.addChild(verticalLayout, HAlignment.CENTER, VAlignment.CENTER)

        return MosaikApp(rootView).apply {
            actions = listOf(ReloadAction().apply { id = reloadActionId })
            manifest =
                MosaikManifest(
                    "Connection problem?",
                    1,
                    MosaikContext.LIBRARY_MOSAIK_VERSION,
                    cacheLifetime = 0
                )
        }
    }
}