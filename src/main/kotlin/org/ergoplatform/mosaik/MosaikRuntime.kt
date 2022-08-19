package org.ergoplatform.mosaik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.*
import org.ergoplatform.mosaik.model.ui.input.StringTextField

abstract class MosaikRuntime(
    val backendConnector: MosaikBackendConnector,
) {

    abstract val coroutineScope: CoroutineScope

    /**
     * Handler to show and manage modal dialogs. These dialogs are managed outside Mosaik's
     * [ViewTree] so that implementations can use platform's modal dialogs
     */
    abstract fun showDialog(dialog: MosaikDialog)

    abstract fun pasteToClipboard(text: String)

    abstract fun openBrowser(url: String)

    abstract fun onAddressLongPress(address: String)

    abstract fun scanQrCode(actionId: String)

    fun qrCodeScanned(actionId: String, scannedValue: String) {
        val action = viewTree.getAction(actionId) as? QrCodeAction

        action?.let {
            val newParent = action.newContent.view
            if (newParent is StringTextField) {
                newParent.value = scannedValue
            }
            runChangeSiteAction(action)

        }
    }

    /**
     * return fiat amount, if available. null if no fiat amount is available
     * @param formatted if true, fiat value is formatted according to user's locale and with
     * currency; if false, it will be used as an input amount (English decimal, no currency)
     */
    abstract fun convertErgToFiat(nanoErg: Long, formatted: Boolean = true): String?

    abstract val fiatRate: Double?

    abstract var preferFiatInput: Boolean

    /**
     * optional callback to be informed when a MosaikApp is loaded. This is called on fist load
     * as well as every new app loaded via [NavigateAction]
     */
    var appLoaded: ((MosaikManifest) -> Unit)? = null

    /**
     * delay in milliseconds to wait after a user's input into a text field before
     * InputElement.onValueChangedAction is launched.
     */
    var textFieldOnValueChangedActionDelay: Long = 1000

    /**
     * Show error to user. Can be called on background thread
     * Client code can override to show own error texts and change behaviour.
     */
    open fun showError(error: Throwable) {
        showDialog(
            MosaikDialog(
                "Error:\n${error.message}\n(${error.javaClass.simpleName})",
                "OK",
                null,
                null,
                null
            )
        )
    }

    val viewTree = ViewTree(this)
    var appManifest: MosaikManifest? = null
        private set
    private val _appUrlStack = mutableListOf<UrlHistoryEntry>()
    val appUrlHistory: List<String> get() = _appUrlStack.map { it.url }
    val appUrl: String? get() = if (_appUrlStack.isNotEmpty()) appUrlHistory.first() else null

    fun runAction(actionId: String) {
        viewTree.getAction(actionId)?.let { runAction(it) }
    }

    open fun runAction(action: Action) {
        MosaikLogger.logDebug("Running action ${action.id}...")
        try {
            when (action) {
                is QrCodeAction -> {
                    scanQrCode(action.id)
                }
                is ChangeSiteAction -> {
                    runChangeSiteAction(action)
                }
                is DialogAction -> {
                    runDialogAction(action)
                }
                is CopyClipboardAction -> {
                    runCopyClipboardAction(action)
                }
                is BrowserAction -> {
                    runBrowserAction(action)
                }
                is BackendRequestAction -> {
                    runBackendRequest(action)
                }
                is NavigateAction -> {
                    runNavigateAction(action)
                }
                is ReloadAction -> {
                    reloadCurrentApp()
                }
                is ErgoPayAction -> {
                    runErgoPayAction(action)
                }
                is TokenInformationAction -> {
                    runTokenInformationAction(action)
                }
                is ErgoAuthAction -> {
                    runErgoAuthAction(action)
                }
                else -> runUnknownAction(action)
            }
        } catch (e: InvalidValuesException) {
            // show user, do not log an error
            showError(e)
        } catch (e: ChangeViewContentException) {
            raiseError(e)
        } catch (t: Throwable) {
            MosaikLogger.logError("Error running ${action.javaClass.simpleName}", t)
        }
    }

    private fun reloadCurrentApp() {
        loadMosaikApp(appUrl!!)
    }

    abstract fun runTokenInformationAction(action: TokenInformationAction)

    abstract fun runErgoPayAction(action: ErgoPayAction)

    abstract fun runErgoAuthAction(action: ErgoAuthAction)

    open fun runUnknownAction(action: Action) {
        throw UnsupportedOperationException("Action type ${action.javaClass.simpleName} not yet implemented")
    }

    open fun runNavigateAction(action: NavigateAction) {
        loadMosaikApp(action.url, appUrl)
    }

    open fun runBackendRequest(action: BackendRequestAction) {
        if (action.postValues == BackendRequestAction.PostValueType.ALL)
            viewTree.ensureValuesAreCorrect()

        viewTree.uiLocked = true
        coroutineScope.launch(Dispatchers.Default) {
            try {
                val fetchActionResponse =
                    backendConnector.fetchAction(
                        action.url,
                        appUrl,
                        if (action.postValues == BackendRequestAction.PostValueType.NONE) emptyMap()
                        else viewTree.currentValidValues,
                        appUrl
                    )
                val appVersion = fetchActionResponse.appVersion
                val newAction =
                    if (appVersion != appManifest!!.appVersion) ReloadAction().apply { id = "" }
                    else fetchActionResponse.action

                withContext(Dispatchers.Main) {
                    runAction(newAction)
                }
            } catch (ce: ConnectionException) {
                MosaikLogger.logError("Connection error running Mosaik backend request", ce)
                // don't raise an error for connection exceptions
                showError(ce)
            } catch (t: Throwable) {
                MosaikLogger.logError("Error running Mosaik backend request", t)
                raiseError(t)
            }

            viewTree.uiLocked = false
        }

    }

    private fun runCopyClipboardAction(action: CopyClipboardAction) {
        pasteToClipboard(action.text)
    }

    open fun runBrowserAction(action: BrowserAction) {
        openBrowser(action.url)
    }

    open fun runDialogAction(action: DialogAction) {
        showDialog(
            MosaikDialog(
                action.message,
                action.positiveButtonText ?: "OK",
                action.negativeButtonText,
                viewTree.getAction(action.onPositiveButtonClicked)
                    ?.let { { runAction(it) } },
                viewTree.getAction(action.onNegativeButtonClicked)
                    ?.let { { runAction(it) } }
            )
        )
    }

    open fun runChangeSiteAction(action: ChangeSiteAction) {
        try {
            try {
                viewTree.setContentView(action.newContent.view.id, action.newContent)
            } catch (nf: ElementNotFoundException) {
                MosaikLogger.logInfo(
                    "${action.javaClass.simpleName}: element ${nf.elementId} not found, replacing complete view tree",
                    nf
                )
                viewTree.setContentView(null, action.newContent)
            }
        } catch (t: Throwable) {
            // we are probably in a very bad state now, tell the user to reload
            throw ChangeViewContentException(t)
        }
    }

    /**
     * starts loading a new Mosaik app. Please make sure this is not called twice simultaneously.
     */
    open fun loadMosaikApp(url: String, referrer: String? = null) {
        viewTree.uiLocked = true
        coroutineScope.launch(Dispatchers.Default) {
            try {
                val loadAppResponse = backendConnector.loadMosaikApp(url, referrer)
                val mosaikApp = loadAppResponse.mosaikApp
                appManifest = mosaikApp.manifest

                viewTree.setRootView(mosaikApp)
                navigatedTo(UrlHistoryEntry(loadAppResponse.appUrl, referrer), mosaikApp.manifest)
            } catch (ce: ConnectionException) {
                MosaikLogger.logError("Connection error loading Mosaik app", ce)
                // do not raise an error
                showError(ce)
            } catch (t: Throwable) {
                MosaikLogger.logError("Error loading Mosaik app", t)
                raiseError(t)
            }

            viewTree.uiLocked = false
        }
    }

    private fun navigatedTo(urlHistoryEntry: UrlHistoryEntry, manifest: MosaikManifest) {
        if (appUrl != urlHistoryEntry.url)
            _appUrlStack.add(0, urlHistoryEntry)
        appLoaded?.invoke(manifest)
    }

    /**
     * Navigates back to the previous Mosaik App, if any and returns true.
     * Returns false if there us no previous Mosaik app
     */
    open fun navigateBack(): Boolean {
        return if (canNavigateBack()) {
            _appUrlStack.removeFirst()
            val lastApp = _appUrlStack.first()
            loadMosaikApp(lastApp.url, lastApp.referrer)
            true
        } else
            false
    }

    fun canNavigateBack() = _appUrlStack.size >= 2

    /**
     * an error that is shown to user and reported to the error report url
     */
    open fun raiseError(t: Throwable) {
        // TODO report error to error report url
        showError(t)
    }

    /**
     * Downloads content of LazyLoadBox, blocking. In case of an error null is returned
     */
    suspend fun fetchLazyContents(url: String): ViewContent? {
        return try {
            backendConnector.fetchLazyContent(url, appUrl, appUrl!!)
        } catch (t: Throwable) {
            MosaikLogger.logError("Could not fetch content $url", t)
            null
        }
    }

    abstract fun isErgoAddressValid(ergoAddress: String): Boolean

    /**
     * return an address label, if available
     */
    abstract fun getErgoAddressLabel(ergoAddress: String): String?

    /**
     * return wallet label, if available
     */
    abstract fun getErgoWalletLabel(firstAddress: String): String?

    abstract fun formatString(string: StringConstant, values: String? = null): String

    /**
     * open a chooser for an ergo address. If the user sets a new value, this should call
     * [setValue] with the new address.
     */
    abstract fun showErgoAddressChooser(valueId: String)

    /**
     * open a chooser for an ergo wallet. If the user sets a new value, this should call
     * [setValue] with the new address.
     */
    abstract fun showErgoWalletChooser(valueId: String)

    /**
     * set the value for a value element in the current tree. Please note that not all elements
     * are automatically updated by this in the view.
     */
    fun setValue(valueId: String, newValue: Any?) {
        viewTree.findElementById(valueId)?.let { element ->
            if (element.hasValue)
                element.valueChanged(newValue)
        }
    }

    /**
     * checks if the view tree is still valid or if its lifetime is expired. If it is expired,
     * the app is reloaded.
     * Executing apps should call this method on certain occasions, for example when coming
     * from background to foreground, but make sure that the user is not disturbed while interacting
     * with the app
     */
    fun checkViewTreeValidity() {
        val viewTreeLifeTime = appManifest?.cacheLifetime ?: 0
        if (viewTreeLifeTime > 0 && System.currentTimeMillis() - viewTree.lastViewTreeChangeMs > viewTreeLifeTime * 1000L) {
            MosaikLogger.logDebug("Viewtree expired, reloading app")
            reloadCurrentApp()
        }
    }
}

data class MosaikDialog(
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String?,
    val positiveButtonClicked: (() -> Unit)?,
    val negativeButtonClicked: (() -> Unit)?
)

data class UrlHistoryEntry(val url: String, val referrer: String?)

enum class StringConstant {
    ChooseAddress,
    ChooseWallet,
    PleaseChoose,
}