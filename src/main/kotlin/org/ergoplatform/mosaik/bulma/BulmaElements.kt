package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.readOnly
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLabelElement

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
                Span {
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
    label: String?,
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
    attribs: ((InputAttrsScope<String>) -> Unit)? = null,
) {
    Div(attrs = {
        classes("control")
        // TODO Icons
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