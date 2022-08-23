package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*

@Composable
fun BulmaModal(
    backgroundAttrs: ((AttrsScope<HTMLDivElement>) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Div(attrs = {
        classes("modal", "is-active")
    }) {
        Div(attrs = {
            classes("modal-background")
            backgroundAttrs?.invoke(this)
        })
        Div(attrs = {
            classes("modal-content", "p-4")
        }) {
            content()
        }
    }
}

@Composable
fun BulmaBlock(attrs: AttrBuilderContext<HTMLDivElement>? = null, content: @Composable () -> Unit) {
    Div(attrs = {
        classes("block")
        attrs?.invoke(this)
    }) {
        content()
    }
}

/**
 * https://bulma.io/documentation/elements/notification/
 */
@Composable
fun BulmaNotification(
    color: BulmaColor? = null,
    classes: List<String> = emptyList(),
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable () -> Unit
) {
    Div(attrs = {
        classes("notification", *classes.toTypedArray())
        color?.let { classes(color.toCssClassName()) }
        attrs?.invoke(this)
    }) {
        content()
    }
}

@Composable
fun BulmaBox(attrs: AttrBuilderContext<HTMLDivElement>? = null, content: @Composable () -> Unit) {
    Div(attrs = {
        classes("box")
        attrs?.invoke(this)
    }) {
        content()
    }
}

@Composable
fun BulmaButton(
    onClick: () -> Unit,
    text: String,
    color: BulmaColor = BulmaColor.PRIMARY,
    enabled: Boolean = true,
    classes: List<String> = emptyList(),
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
) {
    Button(attrs = {
        onClick { onClick() }
        classes("button", color.toCssClassName(), *classes.toTypedArray())
        if (!enabled) {
            disabled()
        }
        attrs?.invoke(this)
    }) {
        Span(attrs = {
            style { styleTextEllipsis(text.shouldWrapEverywhere()) }
        }) {
            Text(text)
        }
    }
}

fun String.shouldWrapEverywhere() = !contains(' ') && length > 30

fun StyleScope.wrapEverywhere() = property("word-break", "break-all")

private fun StyleScope.styleTextEllipsis(mayBreakAll: Boolean, maxLines: Int = 1) {
    maxWidth(100.percent)
    overflow("hidden")
    property("display", "-webkit-box")
    property("-webkit-line-clamp", maxLines)
    property("-webkit-box-orient", "vertical")

    if (maxLines == 1 && mayBreakAll)
        wrapEverywhere()
}

@Composable
fun BulmaLinkButton(
    href: String,
    text: String,
    color: BulmaColor = BulmaColor.PRIMARY,
    enabled: Boolean = true,
    classes: List<String> = emptyList(),
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
) {
    A(attrs = {
        href(href)
        classes("button", color.toCssClassName(), *classes.toTypedArray())
        if (!enabled) {
            classes("is-static")
        }
        attrs?.invoke(this)
    }) {
        Text(text)
    }
}

@Composable
fun BulmaDropDown(
    label: String,
    elements: List<String>,
    classes: List<String> = emptyList(),
    iconClasses: List<String>,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    onItemClicked: (Int) -> Unit,
) {
    val activeState = remember { mutableStateOf(false) }

    Div(attrs = {
        classes("dropdown", *classes.toTypedArray())
        if (activeState.value)
            classes("is-active")

        attrs?.invoke(this)
    }
    ) {

        // the trigger element
        Div(attrs = {
            classes("dropdown-trigger")
            style {
                width(100.percent)
            }
        }) {
            Button(attrs = {
                onClick { activeState.value = !activeState.value }
                classes("button")
                attr("aria-haspopup", "true")
                attr("aria-controls", "true")
                style {
                    width(100.percent)
                }
            }) {
                Span(attrs = {
                    style {
                        styleTextEllipsis(label.shouldWrapEverywhere())
                        whiteSpace("normal") // needed because bulma sets nowrap
                    }
                }) {
                    Text(label)
                }
                Span(attrs = {
                    classes("icon", "is-small")
                }) {
                    I(attrs = {
                        classes(*iconClasses.toTypedArray())
                        attr("aria-hidden", "true")
                    }) {

                    }
                }
            }
        }

        // the menu
        Div(attrs = {
            classes("dropdown-menu")
            attr("role", "menu")
            style {
                width(100.percent)
            }
        }) {
            Div(attrs = {
                classes("dropdown-content")
            }) {
                elements.forEachIndexed { idx, item ->
                    A(attrs = {
                        classes("dropdown-item")
                        onClick {
                            activeState.value = false
                            onItemClicked(idx)
                        }
                        style {
                            styleTextEllipsis(item.shouldWrapEverywhere())
                            whiteSpace("normal") // needed because bulma sets nowrap
                        }
                    }) {
                        Text(item)
                    }
                }
            }
        }
    }
}

@Composable
fun BulmaField(
    label: String? = null,
    helpText: String? = null,
    helpColor: BulmaColor = BulmaColor.PRIMARY,
    classes: List<String> = emptyList(),
    outerAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    labelAttrs: AttrBuilderContext<HTMLLabelElement>? = null,
    content: @Composable () -> Unit
) {
    Div(attrs = {
        classes("field", *classes.toTypedArray())
        outerAttrs?.invoke(this)
    }
    ) {
        label?.let {
            Label(attrs = {
                classes("label")
                labelAttrs?.invoke(this)
            }) {
                Text(label)
            }
        }

        content()

        helpText?.let {
            P(attrs = {
                classes("help", helpColor.toCssClassName())
            }) {

            }
        }
    }
}

@Composable
fun BulmaInput(
    type: BulmaInputType,
    initialValue: String,
    onValueChanged: ((String) -> Unit),
    outlineColor: BulmaColor? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    rightIconClasses: List<String> = emptyList(),
    onRightIconClick: (() -> Unit)? = null,
    attribs: ((InputAttrsScope<String>) -> Unit)? = null,
) {
    Div(attrs = {
        classes("control")
        if (rightIconClasses.isNotEmpty()) {
            classes("has-icons-right")
        }
    }) {
        Input(type.toInputType(), attrs = {
            val classes = listOf("input").toMutableList()

            outlineColor?.let {
                classes.add(outlineColor.toCssClassName())
            }

            classes(*classes.toTypedArray())

            if (!enabled) {
                disabled()
            }
            if (readOnly) {
                readOnly()
            }

            value(initialValue)
            onInput { event -> onValueChanged(event.value) }

            attribs?.invoke(this)
        })

        if (rightIconClasses.isNotEmpty()) {
            Span(attrs = {
                classes("icon", "is-small", "is-right")
                onRightIconClick?.let {
                    classes("is-clickable")
                    onClick { onRightIconClick() }
                }
            }) {
                I(attrs = {
                    classes(*rightIconClasses.toTypedArray())
                }) {

                }
            }
        }
    }
}

enum class BulmaInputType {
    Text, Password, Email, Tel
}

private fun BulmaInputType.toInputType(): InputType<String> =
    when (this) {
        BulmaInputType.Text -> InputType.Text
        BulmaInputType.Password -> InputType.Password
        BulmaInputType.Email -> InputType.Email
        BulmaInputType.Tel -> InputType.Tel
    }


/**
 * https://bulma.io/documentation/form/checkbox/
 */
@Composable
fun BulmaCheckbox(
    checked: Boolean,
    onValueChanged: ((Boolean) -> Unit),
    enabled: Boolean = true,
    labelAttrs: AttrBuilderContext<HTMLLabelElement>? = null,
    checkBoxAttrs: AttrBuilderContext<HTMLInputElement>? = null,
    content: @Composable () -> Unit
) {
    Div(attrs = {
        classes("control")
    }) {

        Label(attrs = {
            classes("checkbox")

            if (enabled)
                onClick { onValueChanged(!checked) }

            if (!enabled)
                attr("disabled", "")

            labelAttrs?.invoke(this)

        }
        ) {
            Input(InputType.Checkbox, attrs = {
                if (!enabled)
                    disabled()

                checked(checked)

                checkBoxAttrs?.invoke(this)
            })
            content()
        }
    }
}

@Composable
fun BulmaProgressbar(
    size: BulmaSize, color: BulmaColor,
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
) {
    Progress(attrs = {
        classes("progress", size.toCssClassName(), color.toCssClassName(), *classes.toTypedArray())
        attribs?.invoke(this)
    }) {

    }
}

/**
 * https://bulma.io/documentation/layout/container/
 */
@Composable
fun BulmaContainer(
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Div(attrs = {
        classes("container", *classes.toTypedArray())
        attribs?.invoke(this)
    }) {
        content()
    }
}

/**
 * https://bulma.io/documentation/components/tabs/
 */
@Composable
fun BulmaTabs(
    captions: List<String>,
    selected: Int?,
    onClick: (Int) -> Unit,
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
) {
    Div(attrs = {
        classes("tabs", *classes.toTypedArray())
        attribs?.invoke(this)
    }) {
        Ul {
            captions.forEachIndexed { index, element ->
                Li(attrs = {
                    if (index == selected)
                        classes("is-active")
                }) {
                    A(attrs = {
                        onClick { onClick(index) }
                    }) {
                        Text(element)
                    }
                }
            }
        }
    }
}

fun BulmaColor.toCssClassName(): String {
    return when (this) {
        BulmaColor.PRIMARY -> "is-primary"
        BulmaColor.LINK -> "is-link"
        BulmaColor.INFO -> "is-info"
        BulmaColor.SUCCESS -> "is-success"
        BulmaColor.WARNING -> "is-warning"
        BulmaColor.DANGER -> "is-danger"
        BulmaColor.DARK -> "is-dark"
        BulmaColor.TEXT -> "is-text"
    }
}

enum class BulmaColor {
    PRIMARY,
    LINK,
    INFO,
    SUCCESS,
    WARNING,
    DANGER,
    DARK,
    TEXT
}


fun BulmaSize.toCssClassName(): String {
    return when (this) {
        BulmaSize.SMALL -> "is-small"
        BulmaSize.NORMAL -> "is-normal"
        BulmaSize.MEDIUM -> "is-medium"
        BulmaSize.LARGE -> "is-large"
    }
}

enum class BulmaSize {
    SMALL,
    NORMAL,
    MEDIUM,
    LARGE
}

const val cssClassHiddenOnMobile = "is-hidden-mobile"