package org.ergoplatform.mosaik.bulma

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.ergoplatform.mosaik.TreeElement
import org.ergoplatform.mosaik.model.ui.ForegroundColor
import org.ergoplatform.mosaik.model.ui.MarkDown
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.listStyle
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

@Composable
fun MosaikMarkDown(
    treeElement: TreeElement,
    classes: List<String>,
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)?,
) {
    val element = treeElement.element as MarkDown
    val content = element.content
    val textAlign = element.contentAlignment

    MarkDown(content, textAlign, classes, attribs)
}

@Composable
fun MarkDown(
    content: String,
    textAlign: HAlignment = HAlignment.START,
    classes: List<String> = emptyList(),
    attribs: ((AttrsScope<out HTMLElement>) -> Unit)? = null,
) {
    val parsedTree = remember(content) {
        val parsedTree = MarkdownParser(GFMFlavourDescriptor()).buildMarkdownTreeFromString(content)
        parsedTree
    }

    Div(attrs = {
        classes(
            *classes.toTypedArray(),
            *LabelStyle.BODY1.toCssClasses().toTypedArray(),
            textAlign.toTextAlignmentCssClass()
        )
        attribs?.invoke(this)
    }) {
        parsedTree.children.forEach { node ->
            if (!node.handleElement(content)) {
                node.children.forEach { child ->
                    child.handleElement(content)
                }
            }
        }
    }
}

@Composable
private fun ASTNode.handleElement(content: String): Boolean {
    var handled = true
    when (type) {
        MarkdownTokenTypes.TEXT -> Text(getTextInNode(content).toString())
        MarkdownTokenTypes.EOL -> {}
//        // MarkdownElementTypes.CODE_FENCE -> MarkdownCodeFence(content, this, colors = colors)
//        // MarkdownElementTypes.CODE_BLOCK -> MarkdownCodeBlock(content, this, colors = colors)
        MarkdownElementTypes.ATX_1 -> MarkdownHeader(
            content,
            this,
            LabelStyle.HEADLINE1
        )
        MarkdownElementTypes.ATX_2 ->
            MarkdownHeader(
                content,
                this,
                LabelStyle.HEADLINE2
            )
        MarkdownElementTypes.ATX_3 ->
            MarkdownHeader(
                content,
                this,
                LabelStyle.BODY1BOLD
            )
        MarkdownElementTypes.ATX_4 ->
            MarkdownHeader(
                content,
                this,
                LabelStyle.BODY2BOLD
            )
        MarkdownElementTypes.ATX_5 ->
            MarkdownHeader(
                content,
                this,
                LabelStyle.BODY1
            )
        MarkdownElementTypes.ATX_6 ->
            MarkdownHeader(
                content,
                this,
                LabelStyle.BODY2
            )
//        //MarkdownElementTypes.BLOCK_QUOTE -> MarkdownBlockQuote(content, this, style = typography.body1, color = colors.textColorByType(
//        //    MarkdownElementTypes.BLOCK_QUOTE
//        //))
        MarkdownElementTypes.PARAGRAPH -> MarkdownParagraph(content, this)
        MarkdownElementTypes.ORDERED_LIST ->
            MarkdownOrderedList(
                content,
                this@handleElement,
            )
        MarkdownElementTypes.UNORDERED_LIST ->
            MarkdownBulletList(
                content,
                this@handleElement,
            )

        MarkdownElementTypes.LINK_DEFINITION -> {
            // nothing to do, browser handles itself
        }
        else -> handled = false
    }
    return handled
}

@Composable
private fun MarkdownHeader(
    content: String,
    node: ASTNode,
    style: LabelStyle,
) {
    P(attrs = {
        classes(*style.toCssClasses().toTypedArray())
    }) {
        node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.let {
            Text(it.getTextInNode(content).trim().toString())
        }

    }
}

@Composable
private fun MarkdownParagraph(
    content: String,
    node: ASTNode,
) {
    P(attrs = {
        classes("m-2")
    }) { MarkDownText(content, node) }
}


@Composable
private fun MarkDownLink(content: String, node: ASTNode) {
    val linkText =
        node.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.children?.innerList() ?: return
    val destination =
        node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)
            ?.toString()
    val linkLabel =
        node.findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(content)?.toString()
    MarkDownLink(destination ?: linkLabel) {
        MarkDownText(content, linkText)
    }
}

@Composable
fun MarkDownLink(
    destination: String?,
    content: @Composable () -> Unit,
) {
    A(destination, attrs = {
        target(ATarget.Blank)
        classes(ForegroundColor.PRIMARY.toCssClass())
    }) {
        content()
    }
}

@Composable
private fun MarkDownAutoLink(content: String, node: ASTNode) {
    val destination = node.getTextInNode(content).toString()
    MarkDownLink(destination) {
        Text(destination)
    }
}

@Composable
private fun MarkDownText(content: String, node: ASTNode) {
    MarkDownText(content, node.children)
}

@Composable
private fun MarkDownText(
    content: String,
    children: List<ASTNode>
) {
    children.forEach { child ->
        when (child.type) {
            MarkdownElementTypes.PARAGRAPH -> Span { MarkDownText(content, child) }
            MarkdownElementTypes.EMPH -> {
                I {
                    MarkDownText(content, child)
                }
            }
            MarkdownElementTypes.STRONG -> {
                B {
                    MarkDownText(content, child)
                }
            }
            MarkdownElementTypes.CODE_SPAN -> {
                Code {
                    MarkDownText(content, child.children.innerList())
                }
            }
            MarkdownElementTypes.AUTOLINK -> MarkDownAutoLink(content, child)
            MarkdownElementTypes.INLINE_LINK -> MarkDownLink(content, child)
            MarkdownElementTypes.SHORT_REFERENCE_LINK -> MarkDownLink(content, child)
            MarkdownElementTypes.FULL_REFERENCE_LINK -> MarkDownLink(content, child)
            MarkdownTokenTypes.TEXT -> Text(child.getTextInNode(content).toString())
            GFMTokenTypes.GFM_AUTOLINK -> if (child.parent == MarkdownElementTypes.LINK_TEXT) {
                Text(child.getTextInNode(content).toString())
            } else MarkDownAutoLink(content, child)
            MarkdownTokenTypes.SINGLE_QUOTE -> Text("\'")
            MarkdownTokenTypes.DOUBLE_QUOTE -> Text("\"")
            MarkdownTokenTypes.LPAREN -> Text("(")
            MarkdownTokenTypes.RPAREN -> Text(")")
            MarkdownTokenTypes.LBRACKET -> Text("[")
            MarkdownTokenTypes.RBRACKET -> Text("]")
            MarkdownTokenTypes.LT -> Text("<")
            MarkdownTokenTypes.GT -> Text(">")
            MarkdownTokenTypes.COLON -> Text(":")
            MarkdownTokenTypes.EXCLAMATION_MARK -> Text("!")
            MarkdownTokenTypes.BACKTICK -> Text("`")
            MarkdownTokenTypes.HARD_LINE_BREAK -> Text("\n\n")
            MarkdownTokenTypes.EOL -> Text("\n")
            MarkdownTokenTypes.WHITE_SPACE -> Text(" ")
        }
    }
}

@Composable
private fun MarkdownBulletList(
    content: String,
    node: ASTNode,
) {
    Ul(attrs = {
        classes("m-2")
        style { listStyle("none") }
    }) {
        MarkdownListItems(content, node) { child ->
            Text("• ")
            MarkDownText(content, child.children.filterNonListTypes())
        }
    }
}

@Composable
private fun MarkdownOrderedList(
    content: String,
    node: ASTNode,
) {
    Ol(attrs = {
        classes("m-2")
        style { listStyle("none") }
    }) {
        MarkdownListItems(content, node) { child ->
            Text(
                child.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)?.getTextInNode(content)
                    .toString()
            )
            MarkDownText(content, child.children.filterNonListTypes())
        }
    }
}

@Composable
private fun MarkdownListItems(
    content: String,
    node: ASTNode,
    item: @Composable (child: ASTNode) -> Unit
) {
    node.children.forEach { child ->
        when (child.type) {
            MarkdownElementTypes.LIST_ITEM -> {
                Li {
                    item(child)
                    when (child.children.last().type) {
                        MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(
                            content,
                            child,
                        )
                        MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(
                            content,
                            child,
                        )
                    }
                }
            }
            MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(
                content,
                child,
            )
            MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(
                content,
                child,
            )
        }
    }
}

/**
 * Helper function to filter out items within a list of nodes, not of interest for the bullet list.
 */
internal fun List<ASTNode>.filterNonListTypes(): List<ASTNode> = this.filter { n ->
    n.type != MarkdownElementTypes.ORDERED_LIST && n.type != MarkdownElementTypes.UNORDERED_LIST && n.type != MarkdownTokenTypes.EOL
}

/**
 * Helper function to drop the first and last element in the children list.
 * E.g. we don't want to render the brackets of a link
 */
internal fun List<ASTNode>.innerList(): List<ASTNode> = this.subList(1, this.size - 1)
