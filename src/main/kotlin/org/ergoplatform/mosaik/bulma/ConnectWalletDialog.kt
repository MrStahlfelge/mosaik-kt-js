package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.ergoplatform.mosaik.js.JsMosaikRuntime
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ConnectWalletDialog(runtime: JsMosaikRuntime) {
    runtime.choseAddressState.value?.let { addressValueId ->

        val modeSelected =
            remember { mutableStateOf(0) } // TODO save selected and preselect next time
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
                            org.ergoplatform.mosaik.model.ui.text.LabelStyle.HEADLINE2.toCssClass(),
                            org.ergoplatform.mosaik.model.ui.layout.HAlignment.CENTER.toTextAlignmentCssClass(),
                        )
                    }) {
                        Text("Connect wallet")
                    }
                }

                BulmaTabs(
                    ConnectionMode.values().toList().map { it.getCaption() },
                    modeSelected.value,
                    onClick = { modeSelected.value = it }
                )

                when (ConnectionMode.values()[modeSelected.value]) {
                    ConnectionMode.Manually -> ConnectManuallySection(runtime, addressSelected)
                    ConnectionMode.ErgoPay -> {}
                    ConnectionMode.BrowserExtension -> {}
                }

                Div(attrs = {
                    classes("buttons", "is-right")
                }) {

                    BulmaButton(
                        {
                            runtime.choseAddressState.value = null
                        },
                        "Cancel",
                        org.ergoplatform.mosaik.model.ui.text.Button.ButtonStyle.SECONDARY.toBulmaColor(),
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

    BulmaBlock {
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