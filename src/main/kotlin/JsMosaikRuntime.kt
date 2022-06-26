import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.ergoplatform.mosaik.MosaikBackendConnector
import org.ergoplatform.mosaik.MosaikDialog
import org.ergoplatform.mosaik.MosaikRuntime
import org.ergoplatform.mosaik.StringConstant
import org.ergoplatform.mosaik.bulma.MosaikComposeDialogHandler
import org.ergoplatform.mosaik.model.FetchActionResponse
import org.ergoplatform.mosaik.model.ViewContent
import org.ergoplatform.mosaik.model.actions.ErgoAuthAction
import org.ergoplatform.mosaik.model.actions.ErgoPayAction
import org.ergoplatform.mosaik.model.actions.TokenInformationAction
import org.ergoplatform.serialization.MosaikSerializers

class JsMosaikRuntime(private val dialogHandler: MosaikComposeDialogHandler): MosaikRuntime(JsBackendConnector()) {
    override val coroutineScope: CoroutineScope
        get() = MainScope()

    override fun showDialog(dialog: MosaikDialog) {
        println("show dialog: ${dialog.message}")
        dialogHandler.showDialog(dialog)
    }

    override fun pasteToClipboard(text: String) {
        // TODO
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
        window.document.open(url, "_blank")
    }

    override val fiatRate = 2.5

    override fun convertErgToFiat(
        nanoErg: Long,
        withCurrency: Boolean
    ): String? {
        TODO()
    }

    override var preferFiatInput: Boolean = true

    override fun isErgoAddressValid(ergoAddress: String): Boolean {
        // this is just for the desktop demo...
        return ergoAddress.startsWith('9') || ergoAddress.startsWith('3')
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


class JsBackendConnector: MosaikBackendConnector {
    private val client = HttpClient()

    override suspend fun loadMosaikApp(url: String, referrer: String?): MosaikBackendConnector.AppLoaded {
        val response: HttpResponse = client.request(url) {
            method = HttpMethod.Get
        }

        val mosaikApp = MosaikSerializers.parseMosaikAppFromJson(response.readText())
        return MosaikBackendConnector.AppLoaded(selectorApp(mosaikApp.manifest.appName), url)
    }

    override fun fetchAction(
        url: String,
        baseUrl: String?,
        values: Map<String, Any?>,
        referrer: String?
    ): FetchActionResponse {
        TODO("Not yet implemented")
    }

    override fun fetchLazyContent(url: String, baseUrl: String?, referrer: String): ViewContent {
        TODO("Not yet implemented")
    }

}