package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.MosaikDialog
import org.ergoplatform.mosaik.model.ui.text.Button
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.whiteSpace
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * MosaikComposeDialog is an optional component when message dialogs should be handled by
 * Compose. [MosaikComposeDialogHandler] used here is to be used for [MosaikRuntime.showDialog].
 *
 * Alternatively, you can use your platform's native dialog system to show the message dialog.
 */
@Composable
fun MosaikComposeDialog(dialog: MosaikComposeDialogHandler) {
    val dialogState = dialog.flow.collectAsState()

    dialogState.value?.let { mosaikDialog ->
        BulmaModal {
            BulmaBox(attrs = {
                style { padding(0.5.em) }
            }) {
                P(attrs = {
                    style {
                        whiteSpace("pre-line")
                        padding(1.em)
                    }
                }) {
                    Text(mosaikDialog.message)
                }

                BulmaContainer(
                    listOf(
                        "is-flex",
                        "is-flex-direction-row",
                        "is-flex-wrap-nowrap",
                        "is-justify-content-end"
                    ), attribs = {
                        it.style { }
                    }) {
                    mosaikDialog.negativeButtonText?.let { negativeButtonText ->
                        BulmaButton(
                            {
                                dialog.dismiss()
                                mosaikDialog.negativeButtonClicked?.invoke()
                            },
                            negativeButtonText,
                            Button.ButtonStyle.SECONDARY.toBulmaColor(),
                            classes = listOf("m-2")
                        )
                    }

                    BulmaButton(
                        {
                            dialog.dismiss()
                            mosaikDialog.positiveButtonClicked?.invoke()
                        }, mosaikDialog.positiveButtonText,
                        classes = listOf("m-2")
                    )

                }
            }
        }
    }
}

/**
 * Links [MosaikRuntime] with [MosaikComposeDialog]. Use [MosaikComposeDialogHandler.showDialog]
 * for [MosaikRuntime] and pass this object to [MosaikComposeDialog].
 */
class MosaikComposeDialogHandler {
    private val _stateFlow = MutableStateFlow<MosaikDialog?>(null)
    val flow: StateFlow<MosaikDialog?> get() = _stateFlow

    val showDialog: ((MosaikDialog) -> Unit) get() = { _stateFlow.value = it }

    fun dismiss() {
        _stateFlow.value = null
    }
}