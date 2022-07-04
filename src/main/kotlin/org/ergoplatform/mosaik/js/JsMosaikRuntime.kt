package org.ergoplatform.mosaik.js

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ergoplatform.mosaik.MosaikBackendConnector
import org.ergoplatform.mosaik.MosaikDialog
import org.ergoplatform.mosaik.MosaikRuntime
import org.ergoplatform.mosaik.StringConstant
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
    override val coroutineScope: CoroutineScope
        get() = MainScope()

    override fun showDialog(dialog: MosaikDialog) {
        println("show dialog: ${dialog.message}")
        dialogHandler.showDialog(dialog)
    }

    override fun pasteToClipboard(text: String) {
        window.navigator.clipboard.writeText(text)
    }

    override fun runErgoPayAction(action: ErgoPayAction) {
        showDialog(
            MosaikDialog(
                "An ErgoPay action should be run:\n${action.url}",
                "Ok, was done!",
                "Cancel",
                { action.onFinished?.let { runAction(it) } },
                null
            )
        )
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

    override val fiatRate = null

    override fun convertErgToFiat(
        nanoErg: Long,
        withCurrency: Boolean
    ): String? {
        TODO()
    }

    override var preferFiatInput: Boolean = true

    override fun isErgoAddressValid(ergoAddress: String): Boolean {
        return true // no more validation here
    }

    override fun getErgoAddressLabel(ergoAddress: String): String? {
        return if (ergoAddress.startsWith('9'))
            "Mainnet $ergoAddress"
        else if (ergoAddress.startsWith('3'))
            "Testnet $ergoAddress"
        else null
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
        // TODO
    }

    override fun showErgoWalletChooser(valueId: String) {
        // TODO
    }

    override fun scanQrCode(actionId: String) {
        // TODO
    }

    override fun runTokenInformationAction(action: TokenInformationAction) {
        openBrowser("https://explorer.ergoplatform.com/en/token/${action.tokenId}")
    }
}


object JsBackendConnector : MosaikBackendConnector {
    private val client = HttpClient()

    override suspend fun loadMosaikApp(
        url: String,
        referrer: String?
    ): MosaikBackendConnector.AppLoaded {
        val response: HttpResponse = client.request(url) {
            method = HttpMethod.Get
            mosaikContextHeaders.forEach {
                header(it.key, it.value)
            }
        }

        val mosaikApp = MosaikSerializers.parseMosaikAppFromJson(response.readText())
        return MosaikBackendConnector.AppLoaded(mosaikApp, url)
    }

    suspend fun getMosaikConfig(url: String): MosaikConfiguration {
        val response: HttpResponse = client.request(url) {
            method = HttpMethod.Get
        }

        return Json.decodeFromString<MosaikConfiguration>(response.readText())
    }

    override suspend fun fetchAction(
        url: String,
        baseUrl: String?,
        values: Map<String, Any?>,
        referrer: String?
    ): FetchActionResponse {
        val response: HttpResponse = client.request(makeAbsoluteUrl(baseUrl, url)) {
            method = HttpMethod.Post
            mosaikContextHeaders.forEach {
                header(it.key, it.value)
                contentType(ContentType.Application.Json)
            }
            body = MosaikSerializers.valuesMapToJson(values)
        }

        return MosaikSerializers.parseFetchActionResponseFromJson(response.readText())
    }

    private fun makeAbsoluteUrl(baseUrl: String?, url: String): String {
        val loadUrl = if (baseUrl == null || url.contains("://")) url
        else baseUrl.trimEnd('/') + "/" + url.trimStart('/')
        return loadUrl
    }

    override fun fetchLazyContent(url: String, baseUrl: String?, referrer: String): ViewContent {
        TODO("Not yet implemented - use makeAbsoluteUrl")
    }

    private lateinit var mosaikContextHeaders: Map<String, String?>

    fun setContextHeaders(context: MosaikContext) {
        mosaikContextHeaders = MosaikSerializers.contextHeadersMap(context)
    }
}

@Serializable
data class MosaikConfiguration(
    var starturl: String,
    var routes: Map<String, String>? = null
)