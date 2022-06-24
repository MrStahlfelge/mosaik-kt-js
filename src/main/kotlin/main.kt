import androidx.compose.runtime.*
import org.ergoplatform.mosaik.bulma.MosaikViewTree
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    val runtime = JsMosaikRuntime()

    runtime.loadMosaikApp("")
    val viewTree = runtime.viewTree

    require("./custom.scss")
    renderComposable(rootElementId = "root") {
        MosaikViewTree(viewTree)
    }
}

public external fun require(module: String): dynamic

@Composable
fun Body() {
    var counter by remember { mutableStateOf(0) }

    Div(attrs = {
        classes("box")
    }) {
        Div {
            Text("Clicked: ${counter}")
        }
        Button(attrs = { onClick { counter++ }
            classes("button", "is-primary")
        }) {
            Text("Click")
        }
    }
}
