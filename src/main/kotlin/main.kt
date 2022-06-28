import androidx.compose.runtime.mutableStateOf
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.ergoplatform.mosaik.bulma.MosaikComposeDialog
import org.ergoplatform.mosaik.bulma.MosaikComposeDialogHandler
import org.ergoplatform.mosaik.bulma.MosaikViewTree
import org.ergoplatform.mosaik.model.MosaikManifest
import org.jetbrains.compose.web.renderComposable

fun main() {
    val dialogHandler = MosaikComposeDialogHandler()
    val runtime = JsMosaikRuntime(dialogHandler)

    MainScope().launch {
        val config = JsBackendConnector.getMosaikConfig("mosaik.config")
        document.getElementById("loadingScreen")?.remove()
        runtime.loadMosaikApp(config.starturl)
    }


    val viewTree = runtime.viewTree
    val manifestState = mutableStateOf<MosaikManifest?>(null)
    runtime.appLoaded = {
        manifestState.value = it
        document.title = it.appName
    }

    require("./custom.scss")
    renderComposable(rootElementId = "root") {
        MosaikViewTree(viewTree)
        MosaikComposeDialog(dialogHandler)
    }
}

external fun require(module: String): dynamic
