package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.ergoplatform.mosaik.MosaikDialog
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
            BulmaBox {
                P { Text(mosaikDialog.message) }

                BulmaButton({ dialog.dismiss() }, "OK")
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