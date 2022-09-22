package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import org.ergoplatform.mosaik.TreeElement
import org.ergoplatform.mosaik.model.ui.layout.Grid
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.w3c.dom.HTMLElement

@Composable
internal fun MosaikGrid(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as Grid

    // Box: It is a container with relative set.
    // the first child is placed inside the container so that it wraps this child.
    // All other children are positioned absolute (outside the flow) with top/left/right/bottom

    BulmaContainer(classes, attribs = {
        val minWidth = when (element.elementSize) {
            Grid.ElementSize.MIN -> 190 // small phones have around 400px
            Grid.ElementSize.SMALL -> 310
            Grid.ElementSize.MEDIUM -> 420 // 1344 is Bulma's max width / 3
            Grid.ElementSize.LARGE -> 650 // 1344 is Bulma's max width / 2
        }

        it.style {
            fillMaxWidth()
            display(DisplayStyle.Grid)
            gridTemplateColumns("repeat(auto-fit, minmax(min(${minWidth}px, 100%), 1fr))")
        }
        attribs?.invoke(it)
    }) {
        treeElement.children.forEachIndexed { idx, childElement ->
            key(childElement.idOrUuid) {

                MosaikTreeElement(
                    childElement,
                    attribs = {
                        it.style {
                            fillMaxWidth()
                            fillMaxHeight()
                        }
                    },
                    sizeToParent = true
                )

            }
        }
    }
}

