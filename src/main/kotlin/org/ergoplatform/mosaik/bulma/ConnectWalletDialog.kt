package org.ergoplatform.mosaik.bulma

import Uuid
import androidx.compose.runtime.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.ergoplatform.BrowserWallet
import org.ergoplatform.mosaik.MosaikDialog
import org.ergoplatform.mosaik.QRCodeElement
import org.ergoplatform.mosaik.js.ConnectWalletErgoPayConfig
import org.ergoplatform.mosaik.js.JsMosaikRuntime
import org.ergoplatform.mosaik.js.MosaikConfiguration
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement
import kotlin.math.max

@Composable
fun ConnectWalletDialog(runtime: JsMosaikRuntime) {
    val lastModeSaveKey = "walletConnMode"

    runtime.choseAddressState.value?.let { addressValueId ->

        val modeSelected =
            remember {
                val lastConnMode = localStorage.getItem(lastModeSaveKey)?.let {
                    try {
                        ConnectionMode.valueOf(it)
                    } catch (t: Throwable) {
                        null
                    }
                }
                mutableStateOf(max(enabledConnectionModes.indexOf(lastConnMode), 0))
            }
        val treeElement =
            remember(addressValueId) { runtime.viewTree.findElementById(addressValueId) }

        val isWalletChooser = (treeElement?.element is WalletChooseButton)

        val addressSelected =
            remember(treeElement) { mutableStateOf((if (isWalletChooser) (treeElement?.currentValue as? List<*>?)?.firstOrNull() else treeElement?.currentValue) as? String?) }

        BulmaModal {
            BulmaBox(attrs = {
                style { padding(1.em) }
            }) {
                BulmaBlock {
                    P(attrs = {
                        classes(
                            *LabelStyle.HEADLINE2.toCssClasses().toTypedArray(),
                            HAlignment.CENTER.toTextAlignmentCssClass(),
                        )
                    }) {
                        Text("Connect wallet")
                    }
                }

                if (enabledConnectionModes.size > 1)
                    BulmaTabs(
                        enabledConnectionModes.map { it.getCaption() },
                        modeSelected.value,
                        onClick = { newConnMode ->
                            modeSelected.value = newConnMode
                            localStorage.setItem(
                                lastModeSaveKey,
                                enabledConnectionModes[modeSelected.value].toString()
                            )
                        }
                    )

                if (enabledConnectionModes.isEmpty()) {
                    Text("No wallet connection mode set up in Mosaik configuration.")
                } else {
                    when (enabledConnectionModes[modeSelected.value]) {
                        ConnectionMode.Manually -> ConnectManuallySection(runtime, addressSelected)
                        ConnectionMode.ErgoPay -> ConnectErgoPaySection(addressSelected)
                        ConnectionMode.BrowserExtension -> ConnectBrowserWalletSection(
                            runtime,
                            addressSelected
                        )
                    }
                }

                Div(attrs = {
                    classes("buttons", "is-right")
                }) {

                    BulmaButton(
                        {
                            runtime.choseAddressState.value = null
                        },
                        "Cancel",
                        Button.ButtonStyle.SECONDARY.toBulmaColor(),
                        classes = listOf("m-2")
                    )

                    BulmaButton(
                        {
                            if (addressSelected.value != null) {
                                runtime.choseAddressState.value = null
                                if (isWalletChooser)
                                    runtime.setValue(addressValueId, listOf(addressSelected.value))
                                else
                                    runtime.setValue(addressValueId, addressSelected.value)
                            }
                        }, "Done",
                        enabled = addressSelected.value != null,
                        classes = listOf("m-2")
                    )

                }
            }
        }
    }
}

@Composable
private fun ConnectManuallySection(
    runtime: JsMosaikRuntime,
    addressSelected: MutableState<String?>,
) {
    val errorState = remember { mutableStateOf(false) }

    BulmaBlock(attrs = { addIntroTextClasses() }) {
        Text("Please enter or paste your Ergo wallet address here.")
    }

    BulmaField(
        "Wallet address",
        labelAttrs = {
            style {
                marginBottom(0.em) // overwrite Bulma interferences
            }
        },
    ) {

        BulmaInput(
            BulmaInputType.Text,
            addressSelected.value ?: "",
            onValueChanged = {
                if (addressSelected.value != it) {
                    val ergoAddressValid = runtime.isErgoAddressValid(it)
                    errorState.value = !ergoAddressValid
                    addressSelected.value = if (ergoAddressValid) it else null
                }
            },
            if (errorState.value) BulmaColor.DANGER else null,
        )
    }
}

@Composable
fun ConnectErgoPaySection(
    addressSelected: MutableState<String?>,
) {
    if (ergoPayConfig?.isValid() != true) {
        Text("Invalid ConnectWalletErgoPayConfig")
        return
    }

    if (addressSelected.value == null) {
        ConnectErgoPayWaitingBlock(addressSelected)

    } else {
        BulmaBlock(attrs = { addIntroTextClasses() }) {
            Text("Selected address: ${addressSelected.value}")

            Div(attrs = {
                classes("buttons", "is-centered")
            }) {
                BulmaButton(
                    { addressSelected.value = null },
                    "Reset address",
                    Button.ButtonStyle.SECONDARY.toBulmaColor(),
                )
            }
        }

    }
}

private fun AttrsScope<HTMLDivElement>.addIntroTextClasses() {
    classes(
        HAlignment.CENTER.toTextAlignmentCssClass(),
        *LabelStyle.BODY1.toCssClasses().toTypedArray()
    )
}

@Composable
private fun ConnectErgoPayWaitingBlock(addressSelected: MutableState<String?>) {
    val requestId = remember { Uuid.uuid() }
    val ergoPayUrl = remember(requestId) {
        ergoPayConfig!!.ergoPaySetAddressUrl.replace(
            placeHolderRequestId,
            requestId
        )
    }
    val addressRequestSucceeded = remember { mutableStateOf(true) }

    LaunchedEffect(requestId) {
        val client = HttpClient()
        val url = ergoPayConfig!!.getAddressForSessionUrl.replace(placeHolderRequestId, requestId)
        while (addressSelected.value == null && isActive) {
            delay(2000)
            addressRequestSucceeded.value = try {
                val response: HttpResponse = client.request(url) { method = HttpMethod.Get }
                if (response.status.isSuccess()) {
                    val mightBeAddress = response.readText()
                    if (mightBeAddress.isNotBlank())
                        addressSelected.value = mightBeAddress
                    true
                } else
                    false
            } catch (t: Throwable) {
                false
            }
        }
    }

    BulmaBlock(attrs = { addIntroTextClasses() }) {
        if (ergoPayConfig?.hintMarkDown != null) {
            MarkDown(ergoPayConfig!!.hintMarkDown!!, HAlignment.CENTER)
        } else {
            Text("Connect with ErgoPay")
        }
    }

    Div(attrs = {
        classes("buttons", "is-centered")
    }) {
        BulmaLinkButton(ergoPayUrl, "Open external wallet")

        BulmaButton(
            { window.navigator.clipboard.writeText(ergoPayUrl) },
            "Copy request",
            Button.ButtonStyle.SECONDARY.toBulmaColor(),
            classes = listOf(cssClassHiddenOnMobile),
        )
    }

    BulmaBlock {
        P(attrs = {
            classes(
                HAlignment.CENTER.toTextAlignmentCssClass(),
                cssClassHiddenOnMobile
            )
        }) {
            Text("or scan the QR code:")
            Br()
            QRCodeElement(ergoPayUrl)
        }
    }

    BulmaBlock(attrs = {
        classes(
            "is-flex",
            "is-flex-direction-column",
            "is-align-items-center"
        )
    }) {
        if (addressRequestSucceeded.value)
            Text("Waiting for you to connect with ErgoPay...")
        else
            Text("Could not reach service - check your connection. Retrying...")

        BulmaProgressbar(BulmaSize.SMALL, BulmaColor.PRIMARY, attribs = {
            it.style { maxWidth(80.percent) }
        })
    }
}

@Composable
fun ConnectBrowserWalletSection(
    runtime: JsMosaikRuntime,
    addressSelected: MutableState<String?>,
) {

    BulmaBlock(attrs = { addIntroTextClasses() }) {
        if (runtime.mosaikConfig?.browserWalletHintMarkDown != null)
            MarkDown(runtime.mosaikConfig!!.browserWalletHintMarkDown!!, HAlignment.CENTER)
        else
            Text("Connect to a browser extension wallet to set your Ergo address.")
    }

    Div(attrs = {
        classes("buttons", "is-centered")
    }) {
        BulmaButton(
            onClick = {
                MainScope().launch {
                    try {
                        addressSelected.value = BrowserWallet.getWalletAddress(true)
                    } catch (t: Throwable) {
                        runtime.showDialog(
                            MosaikDialog(
                                "An error occured: $t",
                                "OK",
                                null,
                                null,
                                null
                            )
                        )
                    }
                }
            },
            addressSelected.value?.let { "Address $it" } ?: "Select address",
            Button.ButtonStyle.SECONDARY.toBulmaColor(),
            attrs = {
                style { maxWidth(80.percent) }
            },
            enabled = remember { BrowserWallet.isBrowserWalletInstalled() }
        )
    }
}

private enum class ConnectionMode {
    Manually,
    ErgoPay,
    BrowserExtension,
}

private fun ConnectionMode.getCaption() = when (this) {
    ConnectionMode.Manually -> "manually"
    ConnectionMode.ErgoPay -> "ErgoPay"
    ConnectionMode.BrowserExtension -> "Browser wallet"
}

private lateinit var enabledConnectionModes: List<ConnectionMode>
private var ergoPayConfig: ConnectWalletErgoPayConfig? = null

object ConnectWalletDialog {
    fun setConfig(config: MosaikConfiguration) {
        enabledConnectionModes = ConnectionMode.values().toList().filter {
            (it != ConnectionMode.Manually || config.chooseWalletManually)
                    && (it != ConnectionMode.BrowserExtension || config.chooseWalletWithExtension)
                    && (it != ConnectionMode.ErgoPay || config.chooseWalletErgoPay != null)
        }
        ergoPayConfig = config.chooseWalletErgoPay
    }
}

private const val placeHolderRequestId = "#RID#"

private fun ConnectWalletErgoPayConfig.isValid() =
    ergoPaySetAddressUrl.contains(placeHolderRequestId) && getAddressForSessionUrl.contains(
        placeHolderRequestId
    )
            && ergoPaySetAddressUrl.contains("#P2PK_ADDRESS#") && ergoPaySetAddressUrl.startsWith("ergopay://")