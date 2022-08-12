package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.ergoplatform.mosaik.QRCodeElement
import org.ergoplatform.mosaik.js.JsMosaikRuntime
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ErgoPayRequestDialog(runtime: JsMosaikRuntime) {
    runtime.ergoPayActionState.value?.let { ergoPayAction ->

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
                        Text("ErgoPay request")
                    }
                }
                BulmaBlock {
                    P(attrs = {
                        classes(
                            *LabelStyle.BODY1.toCssClasses().toTypedArray(),
                            HAlignment.CENTER.toTextAlignmentCssClass(),
                        )
                    }) {
                        Text("Complete the action with an ErgoPay compatible wallet.")
                        // TODO link to wallets
                    }
                }

                Div(attrs = {
                    classes("buttons", "is-centered")
                }) {
                    BulmaLinkButton(ergoPayAction.url, "Open wallet")

                    BulmaButton(
                        { window.navigator.clipboard.writeText(ergoPayAction.url) },
                        "Copy request",
                        Button.ButtonStyle.SECONDARY.toBulmaColor(),
                        classes = listOf(cssClassHiddenOnMobile),
                    )
                }

                BulmaBlock {
                    P(attrs = {
                        classes(
                            *LabelStyle.BODY1.toCssClasses().toTypedArray(),
                            HAlignment.CENTER.toTextAlignmentCssClass(),
                            cssClassHiddenOnMobile
                        )
                    }) {
                        Text("or scan the QR code:")
                        Br()
                        QRCodeElement(ergoPayAction.url)
                    }
                }

                Div(attrs = {
                    classes("buttons", "is-right")
                }) {

                    BulmaButton(
                        {
                            runtime.ergoPayActionState.value = null
                        },
                        "Cancel",
                        Button.ButtonStyle.SECONDARY.toBulmaColor(),
                        classes = listOf("m-2")
                    )

                    BulmaButton(
                        {
                            runtime.ergoPayActionState.value = null
                            ergoPayAction.onFinished?.let { runtime.runAction(it) }
                        }, "Done",
                        classes = listOf("m-2")
                    )

                }
            }
        }
    }
}