import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    require("./custom.scss")
    renderComposable(rootElementId = "root") {
        Body()
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
