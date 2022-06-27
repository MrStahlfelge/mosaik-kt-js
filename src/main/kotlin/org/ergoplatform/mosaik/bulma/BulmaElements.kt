package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement

@Composable
fun BulmaModal(content: @Composable () -> Unit) {
    Div(attrs = {
        classes("modal", "is-active")
    }) {
        Div(attrs = {
            classes("modal-background")
        })
        Div(attrs = {
            classes("modal-content", "p-4")
        }) {
            content()
        }
    }
}

@Composable
fun BulmaBox(content: @Composable () -> Unit) {
    Div(attrs = {
        classes("box")
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
    attrs: AttrBuilderContext<HTMLButtonElement>? = null,
) {
    Button(attrs = {
        onClick { onClick() }
        classes("button", color.toCssClassName())
        if (!enabled) {
            disabled()
        }
        attrs?.invoke(this)
    }) {
        Text(text)
    }
}

@Composable
fun BulmaProgressbar(size: BulmaSize, color: BulmaColor) {
    Progress(attrs = {
        classes("progress", size.toCssClassName(), color.toCssClassName())
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