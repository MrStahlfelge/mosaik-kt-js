package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.*
import mikhaylutsyury.kigdecimal.toBigDecimal
import org.ergoplatform.mosaik.*
import org.ergoplatform.mosaik.model.MosaikManifest
import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.*
import org.ergoplatform.mosaik.model.ui.layout.*
import org.ergoplatform.mosaik.model.ui.text.*
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputMode
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
        val positionAtTop = viewTreeRoot.element is Column

        val content: @Composable (subStyle: ((StyleScope) -> Unit)?) -> Unit = { subStyle ->
            MosaikTreeElement(
                viewTreeRoot,
                attribs = { atts ->
                    val maxWidth: CSSNumeric? = when (viewTree.targetCanvasDimension) {
                        MosaikManifest.CanvasDimension.COMPACT_WIDTH -> 640.px
                        MosaikManifest.CanvasDimension.MEDIUM_WIDTH -> 900.px
                        else -> null
                    }

                    atts.style {
                        maxWidth?.let {
                            maxWidth(maxWidth)
                            if (positionAtTop)
                                property("margin", "0 auto") // center horizontally
                            else
                                property("margin", "auto") // center
                        }
                        subStyle?.invoke(this)
                    }
                },
                sizeToParent = false
            )
        }

        if (!positionAtTop) {
            DivWrapper(emptyList(), attribs = {
                it.style {
                    position(Position.Absolute)
                    display(DisplayStyle.Flex)
                    bottom(0.px)
                    top(0.px)
                    left(0.px)
                    right(0.px)
                    flexDirection(FlexDirection.Column)
                }
            }) {
                content {
                    it.flex("0 1 auto")
                }
            }
        } else {
            content(null)
        }
    }
    if (locked) {
        BulmaModal(backgroundAttrs = {
            it.style { backgroundColor(Color("#f5f5f588")) }
        }) {
            BulmaProgressbar(BulmaSize.SMALL, BulmaColor.PRIMARY)
        }
    }
}

@Composable
fun MosaikTreeElement(
    treeElement: TreeElement,
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
    sizeToParent: Boolean, // width: 100% already included in attribs, but some elements need further treatment
) {
    val element = treeElement.element

    val moreClasses = classes.toMutableList()

    if (element is LayoutElement) {
        moreClasses.add(element.padding.toCssClass())
    }
    if (!element.visible) {
        moreClasses.add("is-invisible") // TODO check
    }
    if (treeElement.respondsToClick) {
        moreClasses.add("is-clickable")
    }

    val newAttribs: ((AttrsScope<out HTMLElement>) -> Unit)? =
        if (treeElement.respondsToClick && treeElement.element !is Button) {
            {
                it.onClick { treeElement.clicked() }
                it.onContextMenu { event ->
                    treeElement.longPressed()
                    event.preventDefault()
                }
                attribs?.invoke(it)
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

        is CheckboxLabel -> MosaikCheckboxLabel(treeElement, moreClasses, newAttribs)

        is StyleableTextLabel<*> -> {
            MosaikLabel(treeElement, moreClasses, newAttribs)
        }

        is TokenLabel -> MosaikTokenLabel(treeElement, moreClasses, newAttribs)

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
        is TextField -> {
            MosaikTextField(treeElement, moreClasses, newAttribs)
        }
        is DropDownList -> {
            MosaikDropDownList(treeElement, moreClasses, newAttribs)
        }
        is LoadingIndicator -> {
            MosaikLoadingIndicator(treeElement, moreClasses, newAttribs)
        }
        is Icon -> {
            MosaikIcon(treeElement, moreClasses, newAttribs)
        }
        is Image -> {
            MosaikImage(treeElement, moreClasses, newAttribs)
        }

        is QrCode -> MosaikQrCode(treeElement, moreClasses, newAttribs)

        is StyleableInputButton ->
            MosaikInputButton(treeElement, moreClasses, newAttribs, sizeToParent)

        is HorizontalRule -> {
            MosaikHorizontalRule(treeElement, moreClasses, newAttribs)
        }

        is MarkDown -> MosaikMarkDown(treeElement, moreClasses, newAttribs)

        is Grid -> MosaikGrid(treeElement, moreClasses, newAttribs)

        else -> {
            Div(attrs = {
                classes(*moreClasses.toTypedArray())
                newAttribs?.invoke(this)
            }) { Text("Unsupported view element: ${element.javaClass.simpleName}") }
        }
    }
}

@Composable
fun MosaikHorizontalRule(
    treeElement: TreeElement, classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as HorizontalRule

    Hr(attrs = {
        classes(
            when (element.vPadding) {
                Padding.NONE -> "my-0"
                Padding.QUARTER_DEFAULT -> "my-1"
                Padding.HALF_DEFAULT -> "my-2"
                Padding.DEFAULT -> "my-4"
                Padding.ONE_AND_A_HALF_DEFAULT -> "my-5"
                Padding.TWICE -> "my-5"
            }, *classes.toTypedArray()
        )
        attribs?.invoke(this)
        style { fillMaxWidth() }
    })
}

@Composable
fun MosaikInputButton(
    treeElement: TreeElement, classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
    sizeToParent: Boolean,
) {
    val element = treeElement.element as StyleableInputButton

    val valueState = treeElement.viewTree.valueState.collectAsState()

    // remember this to not fire up any logic (db access etc) to retrieve the label
    val buttonLabel =
        remember(valueState.value.second[treeElement.element.id]?.inputValue) { treeElement.currentValueAsString }

    // we add a little padding to the buttons to match the style on Compose Desktop/Android
    DivWrapper(
        classes.toMutableList().apply { add(Padding.QUARTER_DEFAULT.toCssClass()) },
        attribs
    ) {
        when (element.style) {

            StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY,
            StyleableInputButton.InputButtonStyle.BUTTON_SECONDARY -> {
                BulmaButton(
                    treeElement::clicked,
                    buttonLabel,
                    color = when (element.style) {
                        StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY -> Button.ButtonStyle.PRIMARY.toBulmaColor()
                        StyleableInputButton.InputButtonStyle.BUTTON_SECONDARY -> Button.ButtonStyle.SECONDARY.toBulmaColor()
                        else -> throw IllegalStateException("Unreachable")
                    },
                    enabled = element.enabled,
                    attrs = {
                        style {
                            if (sizeToParent) {
                                fillMaxWidth()
                            } else {
                                minWidth(96.px * 2)
                            }
                            whiteSpace("normal") // needed because bulma sets to nowrap - this makes p2pk addresses too long
                        }
                    }
                )
            }

            StyleableInputButton.InputButtonStyle.ICON_PRIMARY,
            StyleableInputButton.InputButtonStyle.ICON_SECONDARY -> {
                BulmaIcon(
                    IconType.WALLET,
                    Icon.Size.MEDIUM,
                    if (!element.enabled) ForegroundColor.SECONDARY
                    else if (element.style == StyleableInputButton.InputButtonStyle.ICON_PRIMARY) ForegroundColor.PRIMARY
                    else ForegroundColor.DEFAULT
                )
            }

        }
    }
}

@Composable
fun MosaikQrCode(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as QrCode

    element.content?.let { content ->
        QRCodeElement(content, classes = classes, attribs = attribs)
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
                Image.Size.XXL -> 512.px
            }
            width(cssSizeValue)
            height(cssSizeValue)
            property("object-fit", "contain")
        }
        attribs?.invoke(this)
    })
}

@Composable
fun MosaikIcon(
    treeElement: TreeElement,
    moreClasses: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as Icon

    DivWrapper(moreClasses, attribs) {
        BulmaIcon(element.iconType, element.iconSize, element.tintColor)
    }
}

@Composable
private fun BulmaIcon(
    iconType: IconType,
    iconSize: Icon.Size,
    tintColor: ForegroundColor,
) {
    Span(attrs = {
        classes(
            "icon",
            when (iconSize) {
                Icon.Size.SMALL -> "is-small"
                Icon.Size.MEDIUM -> "is-medium"
                Icon.Size.LARGE -> "is-large"
            },
            tintColor.toCssClass(),
        )
    }) {
        I(attrs = {
            val classesList = iconType.getCssClasses()
            classes(*classesList.toTypedArray())
            when (iconSize) {
                Icon.Size.SMALL -> null
                Icon.Size.MEDIUM -> "mdi-36px"
                Icon.Size.LARGE -> "mdi-48px"
            }?.let { classes(it) }
        })
    }
}

private fun IconType.getCssClasses(): List<String> =
    listOf(
        "mdi",
        when (this) {
            IconType.INFO -> "mdi-information"
            IconType.WARN -> "mdi-alert"
            IconType.ERROR -> "mdi-alert-circle"
            IconType.CONFIG -> "mdi-cog"
            IconType.ADD -> "mdi-plus"
            IconType.EDIT -> "mdi-pencil"
            IconType.REFRESH -> "mdi-refresh"
            IconType.DELETE -> "mdi-delete"
            IconType.CROSS -> "mdi-close"
            IconType.WALLET -> "mdi-wallet"
            IconType.SEND -> "mdi-send"
            IconType.RECEIVE -> "mdi-call-received"
            IconType.MORE -> "mdi-dots-vertical"
            IconType.OPENLIST -> "mdi-menu-down"
            IconType.CHEVRON_UP -> "mdi-chevron-up"
            IconType.CHEVRON_DOWN -> "mdi-chevron-down"
            IconType.COPY -> "mdi-content-copy"
            IconType.BACK -> "mdi-arrow-left"
            IconType.FORWARD -> "mdi-arrow-right"
            IconType.SWITCH -> "mdi-repeat"
            IconType.QR_CODE -> "mdi-qrcode"
            IconType.QR_SCAN -> "mdi-qrcode-scan"
        }
    )

@Composable
fun MosaikLoadingIndicator(
    treeElement: TreeElement,
    moreClasses: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as LoadingIndicator

    BulmaProgressbar(
        when (element.size) {
            LoadingIndicator.Size.SMALL -> BulmaSize.SMALL
            LoadingIndicator.Size.MEDIUM -> BulmaSize.MEDIUM
        },
        BulmaColor.PRIMARY,
        moreClasses,
        attribs,
    )
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
                    flex("0 1 auto")
                    padding(0.px) // "box" has padding, overwrite it
                }
            })
        }
    } else {
        DivWrapper(moreClasses, attribs) {
            MosaikBox(treeElement, listOf("box"), attribs = {
                it.style {
                    padding(0.px) // "box" has padding, overwrite it
                }
            })
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

                            when (element.getChildVAlignment(childElement.element)) {
                                VAlignment.TOP -> top(0.px)
                                VAlignment.CENTER -> {} // nothing to do
                                VAlignment.BOTTOM -> bottom(0.px)
                            }
                        }

                        if (childHAlignment == HAlignment.JUSTIFY) {
                            fillMaxWidth()
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

internal fun StyleScope.fillMaxWidth() {
    width(100.percent)
    boxSizing("border-box")
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
                fillMaxWidth()  // TODO check could mess up when row is child of a row
            }
        }
        if (element.spacing != Padding.NONE) {
            it.style { gap(element.spacing.toCssNumeric()) }
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
    }, attribs = {
        if (element.spacing != Padding.NONE) {
            it.style { gap(element.spacing.toCssNumeric()) }
        }
        attribs?.invoke(it)
    }) {
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
                                flex("0 1 auto")
                            }
                            if (hAlignment == HAlignment.JUSTIFY) {
                                fillMaxWidth()
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
fun MosaikTextField(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
    textFieldState: MutableState<String> = getTextFieldStateForElement(treeElement),
    valueChangeCallback: ((Any?) -> Unit)? = null
) {
    val element = treeElement.element as TextField

    val errorState = remember(treeElement.createdAtContentVersion) { mutableStateOf(false) }
    BulmaField(
        element.placeholder,
        element.errorMessage,
        BulmaColor.DANGER,
        classes = classes,
        outerAttrs = {
            style {
                fillMaxWidth()
                marginBottom(0.2.em)
                marginTop(0.2.em)
            }
            attribs?.invoke(this)
        },
        labelAttrs = {
            style {
                marginBottom(0.em) // overwrite Bulma interferences
            }
        },
    ) {

        BulmaInput(
            if (element is PasswordInputField) BulmaInputType.Password else BulmaInputType.Text,
            textFieldState.value,
            onValueChanged = {
                if (textFieldState.value != it) {
                    errorState.value =
                        !treeElement.changeValueFromInput(it.ifEmpty { null })
                    valueChangeCallback?.invoke(treeElement.currentValue)
                }
                textFieldState.value = it
            },
            if (errorState.value) BulmaColor.DANGER else null,
            element.enabled,
            element.readOnly,
            rightIconClasses = element.endIcon?.getCssClasses() ?: emptyList(),
            onRightIconClick = element.onEndIconClicked?.let { endClickAction ->
                { treeElement.runActionFromUserInteraction(endClickAction) }
            },
            attribs = {
                when (treeElement.keyboardType) {
                    KeyboardType.Text -> {}
                    KeyboardType.Number -> it.inputMode(InputMode.Numeric)
                    KeyboardType.NumberDecimal -> it.inputMode(InputMode.Numeric)
                    KeyboardType.Email -> it.inputMode(InputMode.Email)
                    KeyboardType.Password -> {}
                }

                if (element.onImeAction != null)
                    it.onKeyUp {
                        if (it.key == "Enter") {
                            treeElement.runActionFromUserInteraction(element.onImeAction)
                        }
                    }

            }
        )
    }

}

@Composable
private fun getTextFieldStateForElement(treeElement: TreeElement) =
    // keep everything the user entered, as long as the [ViewTree] is not changed
    remember(treeElement.createdAtContentVersion) {
        val currentValue = treeElement.currentValueAsString
        mutableStateOf(
            currentValue,
        )
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

    // we add a little padding to the buttons to match the style on Compose Desktop
    DivWrapper(
        classes.toMutableList().apply { add(Padding.QUARTER_DEFAULT.toCssClass()) },
        attribs
    ) {
        BulmaButton(
            treeElement::clicked,
            element.text ?: "",
            color = element.style.toBulmaColor(),
            enabled = element.enabled,
            attrs = {
                style {
                    minWidth(128.px)
                    if (sizeToParent) {
                        fillMaxWidth()
                    }
                    whiteSpace("pre-line")
                }
            }
        )
    }
    // TODO maxLines > 1 currently not supported, always truncating

}

internal fun Button.ButtonStyle.toBulmaColor() =
    when (this) {
        Button.ButtonStyle.PRIMARY -> BulmaColor.PRIMARY
        Button.ButtonStyle.SECONDARY -> BulmaColor.DARK
        Button.ButtonStyle.TEXT -> BulmaColor.TEXT
    }

@Composable
private fun MosaikCheckboxLabel(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as CheckboxLabel
    val state = remember(treeElement.createdAtContentVersion) {
        println(treeElement.currentValue)
        mutableStateOf((treeElement.currentValue as Boolean?) ?: false)
    }

    BulmaField(classes = classes, outerAttrs = attribs) {

        BulmaCheckbox(
            checked = state.value,
            onValueChanged = { newVal ->
                state.value = newVal
                treeElement.valueChanged(state.value)
            },
            enabled = element.enabled,
            labelAttrs = {
                applyLabelAttributes(element, element.maxLines)
                attribs?.invoke(this)
            },
            checkBoxAttrs = {
                classes("m-1")
            }
        ) {
            Text(element.text ?: "")
        }
    }
}

private fun AttrsScope<out HTMLElement>.applyLabelAttributes(
    element: StyleableLabel,
    maxLines: Int,
) {
    classes(
        *element.style.toCssClasses().toTypedArray(),
        element.textColor.toCssClass(),
    )

    if (element is StyleableTextLabel<*>)
        classes(element.textAlignment.toTextAlignmentCssClass())

    style {
        whiteSpace("pre-line")
        if (maxLines >= 1) {
            overflow("hidden")
            property("display", "-webkit-box")
            property("-webkit-line-clamp", maxLines.toString())
            property("-webkit-box-orient", "vertical")
        }
    }
}

@Composable
private fun MosaikTokenLabel(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as TokenLabel
    val tokenName = element.tokenName ?: element.tokenId
    val decimals = element.decimals

    val text = remember(treeElement.createdAtContentVersion, tokenName, decimals) {
        (element.amount?.let { amount ->
            amount.toString().toBigDecimal().movePointLeft(decimals).toPlainString() + " "
        } ?: "") + tokenName
    }

    Span(attrs = {
        classes(*classes.toTypedArray())
        applyLabelAttributes(element, 1)
        if (text.shouldWrapEverywhere())
            style { wrapEverywhere() }
        attribs?.invoke(this)
    }) {
        Text(text)
    }

}

@Composable
private fun MosaikLabel(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as StyleableTextLabel<*>
    val text = remember(treeElement.createdAtContentVersion) {
        LabelFormatter.getFormattedText(element, treeElement)
    }

    val expandable = (element is ExpandableElement && element.expandOnClick)
    val expanded = remember { mutableStateOf(false) }

    val maxLines = if (expandable && !expanded.value) 1 else element.maxLines

    if (text != null) {
        P(attrs = {
            if (expandable) {
                onClick { expanded.value = !expanded.value }
            }

            classes(*classes.toTypedArray())

            applyLabelAttributes(element, maxLines)

            style {
                // browser do not break single long words but overflow. this can mess up the whole
                // layout on mobile with ergo addresses. So we tell the browser to break everywhere
                // in case there is no space in a long word or a single line restriction
                if (maxLines == 1 || text.shouldWrapEverywhere())
                    wrapEverywhere()
            }
            attribs?.invoke(this)
        }) {
            Text(text)
        }
    }
}

@Composable
fun MosaikDropDownList(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as DropDownList
    val list = element.entries.toList()
    val label = remember { mutableStateOf(treeElement.currentValueAsString) }

    BulmaDropDown(
        label.value,
        list.map { it.second },
        classes,
        iconClasses = IconType.CHEVRON_DOWN.getCssClasses(),
        attrs = {
            attribs?.invoke(this)
            style {
                marginBottom(0.2.em)
                marginTop(0.2.em)
            }
        },
        onItemClicked = { idx ->
            treeElement.changeValueFromInput(list[idx].first)
            label.value = treeElement.currentValueAsString
        },
    )
}

internal fun ForegroundColor.toCssClass() =
    when (this) {
        ForegroundColor.PRIMARY -> "has-text-primary"
        ForegroundColor.DEFAULT -> "has-text-dark"
        ForegroundColor.SECONDARY -> "has-text-grey"
    }

/**
 * https://bulma.io/documentation/helpers/spacing-helpers/
 */
fun Padding.toCssClass(): String =
    when (this) {
        Padding.NONE -> "p-0"
        Padding.QUARTER_DEFAULT -> "p-1"
        Padding.HALF_DEFAULT -> "p-2"
        Padding.DEFAULT -> "p-4"
        Padding.ONE_AND_A_HALF_DEFAULT -> "p-5"
        Padding.TWICE -> "p-5" // p-6 is too much
    }

/**
 * reflects the values from https://bulma.io/documentation/helpers/spacing-helpers/
 */
fun Padding.toCssNumeric(): CSSNumeric =
    when (this) {
        Padding.NONE -> 0.cssRem
        Padding.QUARTER_DEFAULT -> 0.25.cssRem
        Padding.HALF_DEFAULT -> 0.5.cssRem
        Padding.DEFAULT -> 1.cssRem
        Padding.ONE_AND_A_HALF_DEFAULT -> 1.5.cssRem
        Padding.TWICE -> 2.cssRem
    }

fun LabelStyle.toCssClasses(): List<String> {
    val classList = mutableListOf<String>()

    val textSize = when (this) {
        LabelStyle.BODY1 -> 5
        LabelStyle.BODY1BOLD -> 5
        LabelStyle.BODY1LINK -> 5
        LabelStyle.BODY2 -> 6
        LabelStyle.BODY2BOLD -> 6
        LabelStyle.HEADLINE1 -> 3
        LabelStyle.HEADLINE2 -> 4
    }
    classList.add("is-size-$textSize")
    if (MosaikStyleConfig.responsiveMobileTextSize)
        classList.add("is-size-${textSize + 1}-mobile")

    when (this) {
        LabelStyle.BODY1BOLD -> classList.add("has-text-weight-bold")
        LabelStyle.BODY1LINK -> classList.add("is-underlined")
        LabelStyle.BODY2BOLD -> classList.add("has-text-weight-bold")
        LabelStyle.HEADLINE1 -> classList.add("has-text-weight-bold")
        LabelStyle.HEADLINE2 -> classList.add("has-text-weight-bold")
        LabelStyle.BODY1 -> {}
        LabelStyle.BODY2 -> {}
    }

    return classList
}

fun HAlignment.toTextAlignmentCssClass() =
    when (this) {
        HAlignment.START -> "has-text-left"
        HAlignment.CENTER -> "has-text-centered"
        HAlignment.END -> "has-text-right"
        HAlignment.JUSTIFY -> "has-text-justified"
    }

val justifyCssClassName = "is-align-self-stretch"