package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import org.ergoplatform.mosaik.LabelFormatter
import org.ergoplatform.mosaik.TreeElement
import org.ergoplatform.mosaik.ViewTree
import org.ergoplatform.mosaik.javaClass
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

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
        BulmaModal {
            BulmaProgressbar(BulmaSize.SMALL, BulmaColor.PRIMARY)
        }
    }
}

@Composable
fun MosaikTreeElement(
    treeElement: TreeElement,
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null
) {
    val element = treeElement.element

    val moreClasses = classes.toMutableList()

    if (element is LayoutElement) {
        moreClasses.add(element.padding.toCssClass())
    }
    if (!element.isVisible) {
        moreClasses.add("is-invisible") // TODO check
    }

//            if (treeElement.respondsToClick) TODO
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
        is Box -> {
            // this also deals with LazyLoadBox
            MosaikBox(treeElement, moreClasses, attribs)
        }
        is StyleableTextLabel<*> -> {
            MosaikLabel(treeElement, moreClasses, attribs)
        }
        is Column -> {
            MosaikColumn(treeElement, moreClasses, attribs)
        }
        is Row -> {
            MosaikRow(treeElement, moreClasses, attribs)
        }
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
                classes(*moreClasses.toTypedArray())
                attribs?.invoke(this)
            }) { Text("Unsupported view element: ${element.javaClass.simpleName}") }
        }
    }
}

@Composable
private fun MosaikBox(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as Box

    // Box: It is a container with relative set.
    // the first child is placed inside the container so that it wraps this child.
    // All other children are positioned absolute (outside the flow) with top/left/right/bottom

    BulmaContainer(classes.toMutableList().apply {
        addAll(listOf("is-flex", "is-flex-direction-column", "is-flex-wrap-nowrap"))
    }, attribs = {
        attribs?.invoke(it)
    }) {
        treeElement.children.forEachIndexed { idx, childElement ->
            key(childElement.idOrUuid) {

                val childHAlignment = element.getChildHAlignment(childElement.element)

                val childClasses = if (idx == 0)
                    listOf(
                        when (childHAlignment) {
                            HAlignment.START -> "is-align-self-flex-start"
                            HAlignment.CENTER -> "is-align-self-center"
                            HAlignment.END -> "is-align-self-flex-end"
                            HAlignment.JUSTIFY -> "is-align-self-stretch"
                        }
                    )
                else emptyList()

                val newStyles: ((AttrsScope<out HTMLElement>) -> Unit) = {
                    it.style {
                        if (idx > 0) {
                            position(Position.Absolute)

                            if (childHAlignment == HAlignment.START)
                                left(0.px)
                            else if (childHAlignment == HAlignment.END)
                                right(0.px)
                            else if (childHAlignment == HAlignment.JUSTIFY) {
                                width(100.percent)
                                boxSizing("border-box")
                            }

                            when (element.getChildVAlignment(childElement.element)) {
                                VAlignment.TOP -> top(0.px)
                                VAlignment.CENTER -> {} // nothing to do
                                VAlignment.BOTTOM -> bottom(0.px)
                            }
                        }
                    }
                }

                MosaikTreeElement(
                    childElement,
                    childClasses,
                    newStyles
                )

            }
        }
    }
}

@Composable
private fun MosaikRow(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as Row

    // TODO check need to wrap in flexbox for packed=true on some occasions?

    BulmaContainer(classes.toMutableList().apply {
        addAll(listOf("is-flex", "is-flex-direction-row", "is-flex-wrap-nowrap"))
    }, attribs = {
        attribs?.invoke(it)
        if (!element.isPacked) {
            it.style {
                width(100.percent) // TODO check could mess up when row is child of a row
                boxSizing("border-box")
            }
        }
    }) {
        val childrenWithWeightAndAlignment: List<Triple<TreeElement, Int, VAlignment>> =
            treeElement.children.map { childElement ->
                Triple(
                    childElement,
                    element.getChildWeight(childElement.element),
                    element.getChildAlignment(childElement.element)
                )
            }

        val weightSum = childrenWithWeightAndAlignment.sumOf { it.second }
        val everythingWithWeight = childrenWithWeightAndAlignment.all { it.second > 0 }

        childrenWithWeightAndAlignment.forEach { (childElement, weight, vAlignment) ->
            key(childElement.idOrUuid) {
                MosaikTreeElement(
                    childElement,
                    classes = listOf(
                        when (vAlignment) {
                            VAlignment.TOP -> "is-align-self-flex-start"
                            VAlignment.CENTER -> "is-align-self-center"
                            VAlignment.BOTTOM -> "is-align-self-flex-end"
                        }
                    ),
                    attribs = {
                        it.style {
                            if (everythingWithWeight) {
                                width((weight * 100 / weightSum).percent)
                                boxSizing("border-box")
                            } else if (weight > 0) {
                                flexGrow(weight)
                            } else {
                                flex("initial")
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MosaikColumn(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?
) {
    val element = treeElement.element as Column
    val childrenWithWeightAndAlignment: List<Triple<TreeElement, Int, HAlignment>> =
        treeElement.children.map { childElement ->
            Triple(
                childElement,
                element.getChildWeight(childElement.element),
                element.getChildAlignment(childElement.element)
            )
        }

    BulmaContainer(classes.toMutableList().apply {
        addAll(listOf("is-flex", "is-flex-direction-column", "is-flex-wrap-nowrap"))
    }, attribs) {
        childrenWithWeightAndAlignment.forEach { (childElement, weight, hAlignment) ->
            key(childElement.idOrUuid) {
                MosaikTreeElement(
                    childElement,
                    classes = listOf(
                        when (hAlignment) {
                            HAlignment.START -> "is-align-self-flex-start"
                            HAlignment.CENTER -> "is-align-self-center"
                            HAlignment.END -> "is-align-self-flex-end"
                            HAlignment.JUSTIFY -> "is-align-self-stretch"
                        }
                    ),
                    attribs = {
                        it.style {
                            if (weight > 0) {
                                flexGrow(weight)
                            } else {
                                flex("none")
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MosaikLabel(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as StyleableTextLabel<*>
    val text = LabelFormatter.getFormattedText(element, treeElement)

    if (text != null) {
        P(attrs = {
            classes(
                when (element.style) {
                    LabelStyle.BODY1 -> "is-size-5"
                    LabelStyle.BODY1BOLD -> "is-size-5"
                    LabelStyle.BODY1LINK -> "is-size-5" // TODO
                    LabelStyle.BODY2 -> "is-size-6"
                    LabelStyle.BODY2BOLD -> "is-size-6"
                    LabelStyle.HEADLINE1 -> "is-size-2"
                    LabelStyle.HEADLINE2 -> "is-size-3"
                },
                when (element.textAlignment) {
                    HAlignment.START -> "has-text-left"
                    HAlignment.CENTER -> "has-text-centered"
                    HAlignment.END -> "has-text-right"
                    HAlignment.JUSTIFY -> "has-text-justified"
                },
                when (element.style) {
                    LabelStyle.BODY1 -> "has-text-weight-normal"
                    LabelStyle.BODY1BOLD -> "has-text-weight-bold"
                    LabelStyle.BODY1LINK -> "has-text-weight-normal"
                    LabelStyle.BODY2 -> "has-text-weight-normal"
                    LabelStyle.BODY2BOLD -> "has-text-weight-bold"
                    LabelStyle.HEADLINE1 -> "has-text-weight-bold"
                    LabelStyle.HEADLINE2 -> "has-text-weight-bold"
                },
                when (element.textColor) {
                    ForegroundColor.PRIMARY -> "has-text-primary"
                    ForegroundColor.DEFAULT -> "has-text-dark"
                    ForegroundColor.SECONDARY -> "has-text-grey"
                },
                *classes.toTypedArray()
            )
            attribs?.invoke(this)
        }) {
            Text(
                text,
//            maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
//            overflow = TextOverflow.Ellipsis, TODO https://css-tricks.com/snippets/css/truncate-string-with-ellipsis/
            )
        }
    }
}

fun Padding.toCssClass(): String =
    when (this) {
        Padding.NONE -> "p-0"
        Padding.QUARTER_DEFAULT -> "p-1"
        Padding.HALF_DEFAULT -> "p-2"
        Padding.DEFAULT -> "p-4"
        Padding.ONE_AND_A_HALF_DEFAULT -> "p-5"
        Padding.TWICE -> "p-6"
    }