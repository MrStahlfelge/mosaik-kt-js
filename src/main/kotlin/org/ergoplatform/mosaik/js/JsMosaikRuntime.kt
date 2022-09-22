package org.ergoplatform.mosaik.js

import androidx.compose.runtime.mutableStateOf
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.bulma.MosaikComposeDialogHandler
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.MosaikContext
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.ErgoAuthAction
import org.ergoplatform.mosaik.model.actions.ErgoPayAction
import org.ergoplatform.mosaik.model.actions.TokenInformationAction
import org.ergoplatform.serialization.MosaikSerializers

class JsMosaikRuntime(private val dialogHandler: MosaikComposeDialogHandler) :
    MosaikRuntime(JsBackendConnector) {

    var mosaikConfig: MosaikConfiguration? = null

    val ergoPayActionState = mutableStateOf<ErgoPayAction?>(null)
    val choseAddressState = mutableStateOf<String?>(null)
    val notificationState = mutableStateOf<String?>(null)

    override val coroutineScope: CoroutineScope
        get() = MainScope()

    override fun showDialog(dialog: MosaikDialog) {
        dialogHandler.showDialog(dialog)
    }

    override fun showError(error: Throwable) {
        if (error is ConnectionException) {
            showDialog(
                MosaikDialog(
                    "Could not reach application. Check your connection and try again.", "OK",
                    null, null, null
                )
            )
        } else
            super.showError(error)
    }

    override fun pasteToClipboard(text: String) {
        window.navigator.clipboard.writeText(text)
        notificationState.value = "Copied into clipboard"
    }

    override fun runErgoPayAction(action: ErgoPayAction) {
        ergoPayActionState.value = action
    }

    override fun runErgoAuthAction(action: ErgoAuthAction) {
        showDialog(
            MosaikDialog(
                "An ErgoAuth action should be run:\n${action.url}",
                "Ok, was done!",
                "Cancel",
                { action.onFinished?.let { runAction(it) } },
                null
            )
        )
    }

    override fun openBrowser(url: String) {
        window.open(url, "_blank")
    }

    override val fiatRate: Double? = null

    override fun convertErgToFiat(
        nanoErg: Long,
        formatted: Boolean
    ): String? {
        return null // TODO support fiat value
    }

    override var preferFiatInput: Boolean = true

    private val base58Pattern = Regex("^[A-HJ-NP-Za-km-z1-9]*\$")

    override fun isErgoAddressValid(ergoAddress: String): Boolean {
        // we only do a basic base58 check for addresses here
        return ergoAddress.isNotBlank() && ergoAddress.matches(base58Pattern)
    }

    override fun getErgoAddressLabel(ergoAddress: String): String? {
        return ergoAddress
    }

    override fun getErgoWalletLabel(firstAddress: String): String? =
        getErgoAddressLabel(firstAddress)

    override fun formatString(string: StringConstant, values: String?): String {
        return when (string) {
            StringConstant.ChooseAddress -> "Choose an address..."
            StringConstant.PleaseChoose -> "Please choose an option"
            StringConstant.ChooseWallet -> "Choose a wallet..."
        }
    }

    override fun showErgoAddressChooser(valueId: String) {
        choseAddressState.value = valueId
    }

    override fun showErgoWalletChooser(valueId: String) {
        choseAddressState.value = valueId
    }

    override fun onAddressLongPress(address: String) {
        pasteToClipboard(address)
    }

    override fun scanQrCode(actionId: String) {
        // TODO qr code scanner
        showDialog(MosaikDialog("QR code scanning not supported", "OK", null, null, null))
    }

    override fun runTokenInformationAction(tokenId: String) {
        openBrowser(
            (mosaikConfig?.tokenDetailsUrl
                ?: "https://explorer.ergoplatform.com/en/token/%TOKENID%")
                .replace("%TOKENID%", tokenId)
        )
    }
}


object JsBackendConnector : MosaikBackendConnector {
    private val client = HttpClient()

    override suspend fun loadMosaikApp(
        url: String,
        referrer: String?
    ): MosaikBackendConnector.AppLoaded {
        val response: HttpResponse? = try {
            client.request(url) {
                method = HttpMethod.Get
                mosaikContextHeaders.forEach {
                    header(it.key, it.value)
                }
            }
        } catch (t: Throwable) {
            null
        }

        val mosaikApp =
            response?.let { MosaikSerializers.parseMosaikAppFromJson(response.readText()) }
                ?: NotLoadedApp.getApp(url)
        return MosaikBackendConnector.AppLoaded(mosaikApp, url)
    }

    suspend fun getMosaikConfig(url: String): MosaikConfiguration {
        val response: HttpResponse = client.request(url) {
            method = HttpMethod.Get
        }

        return Json.decodeFromString(response.readText())
    }

    override suspend fun fetchAction(
        url: String,
        baseUrl: String?,
        values: Map<String, Any?>,
        referrer: String?
    ): FetchActionResponse {
        val response: HttpResponse = try {
            client.request(makeAbsoluteUrl(baseUrl, url)) {
                method = HttpMethod.Post
                mosaikContextHeaders.forEach {
                    header(it.key, it.value)
                    contentType(ContentType.Application.Json)
                }
                body = MosaikSerializers.valuesMapToJson(values)
            }
        } catch (t: Throwable) {
            throw ConnectionException(t)
        }

        return MosaikSerializers.parseFetchActionResponseFromJson(response.readText())
    }

    private fun makeAbsoluteUrl(baseUrl: String?, url: String): String {
        val loadUrl = if (baseUrl == null || url.contains("://")) url
        else baseUrl.trimEnd('/') + "/" + url.trimStart('/')
        return loadUrl
    }

    override suspend fun fetchLazyContent(
        url: String,
        baseUrl: String?,
        referrer: String
    ): ViewContent {
        val response: HttpResponse = client.request(makeAbsoluteUrl(baseUrl, url)) {
            method = HttpMethod.Get
            mosaikContextHeaders.forEach {
                header(it.key, it.value)
            }
        }
        return MosaikSerializers.parseViewContentFromJson(response.readText())
    }

    override suspend fun reportError(reportUrl: String, appUrl: String, t: Throwable) {
        try {
            client.request<Unit>(reportUrl) {
                method = HttpMethod.Post
                mosaikContextHeaders.forEach {
                    header(it.key, it.value)
                    contentType(ContentType.Application.Json)
                }
                body = t.stackTraceToString()
            }
        } catch (t: Throwable) {
            // ignore it
        }
    }

    private lateinit var mosaikContextHeaders: Map<String, String?>

    fun setContextHeaders(context: MosaikContext) {
        mosaikContextHeaders = MosaikSerializers.contextHeadersMap(context)
    }
}