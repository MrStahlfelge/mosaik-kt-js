package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.ergoplatform.mosaik.TreeElement
import org.ergoplatform.mosaik.ViewTree
import org.ergoplatform.mosaik.javaClass
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Progress
import org.jetbrains.compose.web.dom.Text

@Composable
fun MosaikViewTree(viewTree: ViewTree) {
    val modification by viewTree.contentState.collectAsState()
    val locked by viewTree.uiLockedState.collectAsState()

    modification.second?.let { viewTreeRoot ->
        // Crossfade animation here caused some elements to not update

        // the view root should be scrollable if it is a column, otherwise it will fill
        // the max height
        // TODO

        MosaikTreeElement(
            viewTreeRoot,
//            Modifier.alpha(if (locked) .3f else 1f).padding(Padding.DEFAULT.toCompose())
//                .align(Alignment.Center).widthIn(
//                    max = when (viewTree.targetCanvasDimension) {
//                        // https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
//                        MosaikManifest.CanvasDimension.COMPACT_WIDTH -> 600.dp
//                        MosaikManifest.CanvasDimension.MEDIUM_WIDTH -> 840.dp
//                        else -> Dp.Unspecified
//                    }
//                )
        )
    }
    if (locked) {

        Div(attrs = {
            classes("modal", "is-active")
        }) {
            Div(attrs = {
                classes("modal-background")
            })
            Div(attrs = {
                classes("modal-content")
            }) {
                Progress(attrs = {
                    classes("progress", "is-small", "is-primary")
                }) {

                }
            }
        }
    }
}

@Composable
fun MosaikTreeElement(treeElement: TreeElement, classes: List<String> = emptyList()) {
    val element = treeElement.element

//    val newModifier = if (element is LayoutElement) {
//        modifier.padding(element.padding.toCompose())
//    } else {
//        modifier
//    }.alpha(if (element.isVisible) 1.0f else 0.0f)
//        .then(
//            if (treeElement.respondsToClick)
//                Modifier.combinedClickable(
//                    onClick = treeElement::clicked,
//                    onLongClick = treeElement::longPressed,
//                ) else Modifier
//        )
//
    when (element) {
//        is Card -> {
//            MosaikCard(newModifier, treeElement)
//        }
//        is Box -> {
//            // this also deals with LazyLoadBox
//            MosaikBox(newModifier, treeElement)
//        }
//        is StyleableTextLabel<*> -> {
//            MosaikLabel(treeElement, newModifier)
//        }
//        is Column -> {
//            MosaikColumn(newModifier, treeElement)
//        }
//        is Row -> {
//            MosaikRow(newModifier, treeElement)
//        }
//        is Button -> {
//            MosaikButton(treeElement, newModifier)
//        }
//        is ErgAmountInputField -> {
//            MosaikErgAmountInputLayout(treeElement, newModifier)
//        }
//        is TextField<*> -> {
//            MosaikTextField(treeElement, newModifier)
//        }
//        is DropDownList -> {
//            MosaikDropDownList(treeElement, newModifier)
//        }
//        is LoadingIndicator -> {
//            MosaikLoadingIndicator(treeElement, newModifier)
//        }
//        is Icon -> {
//            MosaikIcon(treeElement, newModifier)
//        }
//        is Image -> {
//            MosaikImage(treeElement, newModifier)
//        }
//        is ErgoAddressChooseButton -> {
//            MosaikValueChooseButton(treeElement, newModifier)
//        }
//        is WalletChooseButton -> {
//            MosaikValueChooseButton(treeElement, newModifier)
//        }
//        is HorizontalRule -> {
//            MosaikHorizontalRule(treeElement, newModifier)
//        }
        else -> {
            Div(attrs = {
                classes(*classes.toTypedArray())
            }) { Text("Unsupported view element: ${element.javaClass.simpleName}") }
        }
    }
}
