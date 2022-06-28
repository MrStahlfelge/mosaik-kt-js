package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.*
import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.Image
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.Button
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel
import org.ergoplatform.mosaik.model.ui.text.TruncationType
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Composable
fun MosaikViewTree(viewTree: ViewTree) {
    val modification by viewTree.contentState.collectAsState()
    val locked by viewTree.uiLockedState.collectAsState()

    modification.second?.let { viewTreeRoot ->
        // Crossfade animation here caused some elements to not update

        // the view root should be scrollable if it is a column, otherwise it will fill
        // the max height
        val scrollable = viewTreeRoot.element is Column

        val content: @Composable (subStyle: ((StyleScope) -> Unit)?) -> Unit = { subStyle ->
            MosaikTreeElement(
                viewTreeRoot,
                attribs = { atts ->
                    val maxWidth: CSSNumeric? = when (viewTree.targetCanvasDimension) {
                        MosaikManifest.CanvasDimension.COMPACT_WIDTH -> 500.px
                        MosaikManifest.CanvasDimension.MEDIUM_WIDTH -> 840.px
                        else -> null
                    }

                    atts.style {
                        maxWidth?.let {
                            maxWidth(maxWidth)
                        }
                        subStyle?.invoke(this)
                    }
                },
                sizeToParent = false
            )
        }

        if (!scrollable) {
            DivWrapper(emptyList(), attribs = {
                it.style {
                    position(Position.Absolute)
                    display(DisplayStyle.Flex)
                    bottom(0.px)
                    top(0.px)
                    left(0.px)
                    right(0.px)
                    justifyContent(JustifyContent.Center)
                    flexDirection(FlexDirection.Column)
                }
            }) {
                content {
                    it.flex("none")
                }
            }
        } else {
            content(null)
        }
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
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
    sizeToParent: Boolean,
) {
    val element = treeElement.element

    val moreClasses = classes.toMutableList()

    if (element is LayoutElement) {
        moreClasses.add(element.padding.toCssClass())
    }
    if (!element.visible) {
        moreClasses.add("is-invisible") // TODO check
    }

    val newAttribs: ((AttrsScope<out HTMLElement>) -> Unit)? =
        if (treeElement.respondsToClick && treeElement.element !is Button) {
            {
                it.onClick { treeElement.clicked() }
                attribs?.invoke(it)
                // TODO add a hover effect
            }
        } else attribs

    when (element) {
        is Card -> {
            MosaikCard(treeElement, moreClasses, newAttribs, sizeToParent)
        }
        is Box -> {
            // this also deals with LazyLoadBox
            MosaikBox(treeElement, moreClasses, newAttribs)
        }
        is StyleableTextLabel<*> -> {
            MosaikLabel(treeElement, moreClasses, newAttribs)
        }
        is Column -> {
            MosaikColumn(treeElement, moreClasses, newAttribs)
        }
        is Row -> {
            MosaikRow(treeElement, moreClasses, newAttribs)
        }
        is Button -> {
            MosaikButton(treeElement, moreClasses, newAttribs, sizeToParent)
        }
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
        is Image -> {
            MosaikImage(treeElement, moreClasses, newAttribs)
        }
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
                newAttribs?.invoke(this)
            }) { Text("Unsupported view element: ${element.javaClass.simpleName}") }
        }
    }
}

@Composable
fun MosaikImage(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {

    val element = treeElement.element as Image

    Img(element.url, attrs = {
        style {
            classes(*classes.toTypedArray())
            val cssSizeValue = when (element.size) {
                Image.Size.SMALL -> 64.px
                Image.Size.MEDIUM -> 128.px
                Image.Size.LARGE -> 256.px
            }
            width(cssSizeValue)
            height(cssSizeValue)
            property("object-fit", "contain")
        }
        attribs?.invoke(this)
    })
}

@Composable
fun MosaikCard(
    treeElement: TreeElement,
    moreClasses: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
    sizeToParent: Boolean,
) {
    if (!sizeToParent) {
        // need to make one more Div because the padding needs to be outside the box
        DivWrapper(moreClasses.toMutableList().apply { add("is-flex") }, attribs) {
            MosaikBox(treeElement, listOf("box"), attribs = {
                it.style {
                    flex("none")
                }
            })
        }
    } else {
        DivWrapper(moreClasses, attribs) {
            MosaikBox(treeElement, listOf("box"), null)
        }
    }
}

@Composable
fun DivWrapper(
    moreClasses: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
    content: ContentBuilder<HTMLDivElement>
) {
    // need to make one more Div because the padding needs to be outside the box
    Div(attrs = {
        classes(*moreClasses.toTypedArray())
        attribs?.invoke(this)
    }) {
        content()
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
                            HAlignment.JUSTIFY -> justifyCssClassName
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
                    newStyles,
                    sizeToParent = childHAlignment == HAlignment.JUSTIFY,
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
        if (!element.packed) {
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
                    },
                    sizeToParent = (weight > 0),
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
                            HAlignment.JUSTIFY -> justifyCssClassName
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
                    },
                    sizeToParent = hAlignment == HAlignment.JUSTIFY,
                )
            }
        }
    }
}

@Composable
private fun MosaikButton(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
    sizeToParent: Boolean,
) {
    val element = treeElement.element as Button

    remember(element.truncationType) {
        if (element.truncationType != TruncationType.END)
            MosaikLogger.logWarning("TruncationType ignored for button, not supported by this implementation")
    }

    // we add a little padding to the buttons to match the style on Compose Desktop/Android
    DivWrapper(
        classes.toMutableList().apply { add(Padding.QUARTER_DEFAULT.toCssClass()) },
        attribs
    ) {
        BulmaButton(
            treeElement::clicked,
            element.text ?: "",
            color = when (element.style) {
                Button.ButtonStyle.PRIMARY -> BulmaColor.PRIMARY
                Button.ButtonStyle.SECONDARY -> BulmaColor.DARK
                Button.ButtonStyle.TEXT -> BulmaColor.TEXT
            },
            enabled = element.enabled,
            attrs = {
                style {
                    minWidth(128.px)
                    if (sizeToParent) {
                        width(100.percent)
                        boxSizing("border-box")
                    }
                }
            }
        )
    }
    // TODO
//                maxLines = if (element.maxLines <= 0) Int.MAX_VALUE else element.maxLines,
//                textAlign = (when (element.textAlignment) {
//                    HAlignment.START -> TextAlign.Start
//                    HAlignment.CENTER -> TextAlign.Center
//                    HAlignment.END -> TextAlign.End
//                    HAlignment.JUSTIFY -> TextAlign.Justify
//                }),
//                overflow = TextOverflow.Ellipsis,
//            )

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

val justifyCssClassName = "is-align-self-stretch"