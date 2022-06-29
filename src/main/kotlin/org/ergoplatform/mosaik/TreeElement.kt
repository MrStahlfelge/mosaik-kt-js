package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.ViewGroup
import org.ergoplatform.mosaik.model.ui.input.*
import kotlin.random.Random

/**
 * holds a [ViewElement] and wraps it with convenience accessors
 */
class TreeElement(
    val element: ViewElement,
    val parent: TreeElement?,
    val viewTree: ViewTree,
) {
    private val _children = ArrayList<TreeElement>()

    init {
        if (element is ViewGroup) {
            _children.addAll(element.children.map { TreeElement(it, this, viewTree) })
        }
    }

    val hasId get() = element.id != null

    val id get() = element.id

    val idOrUuid by lazy { element.id ?: Random.nextLong(999999999).toString() }

    val hasValue get() = hasId && element is InputElement

    val currentValue
        get() = if (hasValue) {
            viewTree.getCurrentValue(this)?.inputValue
        } else null

    val inputValueHandler: InputElementValueHandler<*>? =
        InputElementValueHandler.getForElement(element, viewTree.mosaikRuntime)

    /**
     * returns the initial value as set by the viewtree
     */
    val initialValue: Any? get() = null // TODO if (hasValue) (element as InputElement).value else null

    /**
     * see [ViewTree.contentVersion]
     * This is not a get() by intention. Content version of the tree when this element was added.
     */
    val createdAtContentVersion = viewTree.contentVersion

    val children: List<TreeElement> get() = _children

    val respondsToClick: Boolean get() = element.onClickAction != null

    override fun equals(other: Any?): Boolean {
        return if (other is TreeElement) {
            element == other.element
        } else false
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }

    fun visitAllElements(visitor: (TreeElement) -> Unit) {
        val queue = mutableListOf(this)

        while (queue.isNotEmpty()) {
            val first = queue.removeFirst()
            visitor(first)
            first._children.forEach { queue.add(it) }
        }
    }

    fun replaceChildElement(replacedElement: TreeElement, newTreeElement: TreeElement) {
        val indexOfChild = _children.indexOf(replacedElement)
        if (indexOfChild < 0) {
            throw IllegalArgumentException("Could not find child to replace")
        }
        _children[indexOfChild] = newTreeElement
        (element as ViewGroup).replaceChild(replacedElement.element, newTreeElement.element)
    }

    fun clicked() {
        viewTree.onItemClicked(this)
    }

    fun longPressed() {
        viewTree.onItemLongClicked(this)
    }

    fun runActionFromUserInteraction(actionId: String?) {
        viewTree.runActionFromUserInteraction(actionId)
    }

    val keyboardType get() = inputValueHandler!!.keyboardType

    /**
     * returns if value is valid
     */
    fun changeValueFromInput(newValue: String?): Boolean {
        return try {
            val nativeValue = inputValueHandler!!.valueFromStringInput(newValue)
            valueChanged(nativeValue)
        } catch (t: Throwable) {
            valueChanged(null, true)
        }
    }

    /**
     * returns if value is valid
     */
    fun valueChanged(newValue: Any?, isInvalid: Boolean = false): Boolean {
        val isValid = if (isInvalid) false else isValueValid(newValue)
        viewTree.onItemValueChanged(this, newValue, isValid)
        return isValid
    }

    fun isValueValid(value: Any?): Boolean {
        return inputValueHandler!!.isValueValid(value)
    }

    val currentValueAsString: String
        get() = inputValueHandler!!.getAsString(currentValue)

    fun getInvalidValueError(): String {
        return when (element) {
            is TextField -> {
                element.errorMessage ?: element.placeholder ?: element.id!!
            }
            else -> {
                id!!
            }
        }
    }
}