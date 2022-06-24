import androidx.compose.runtime.mutableStateOf
import kotlinx.browser.document
import org.ergoplatform.mosaik.bulma.MosaikViewTree
import org.ergoplatform.mosaik.model.MosaikManifest
import org.jetbrains.compose.web.renderComposable

fun main() {
    val runtime = JsMosaikRuntime()

    runtime.loadMosaikApp("http://localhost:8080/appselect")
    val viewTree = runtime.viewTree
    val manifestState = mutableStateOf<MosaikManifest?>(null)
    runtime.appLoaded = {
        manifestState.value = it
        document.title = it.appName
    }

    require("./custom.scss")
    renderComposable(rootElementId = "root") {
        MosaikViewTree(viewTree)
    }
}

external fun require(module: String): dynamic
