package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.*

const val scaleErg = 9
const val scaleformatShortErg = 4

abstract class InputElementValueHandler<T> {
    abstract val keyboardType: KeyboardType
    abstract fun isValueValid(value: Any?): Boolean
    abstract fun valueFromStringInput(value: String?): T?
    open fun getAsString(currentValue: Any?): String = currentValue?.toString() ?: ""

    companion object {
        fun getForElement(
            element: ViewElement,
            mosaikRuntime: MosaikRuntime
        ): InputElementValueHandler<*>? =
            when (element) {
                // TODO is ErgAddressInputField -> ErgAddressTextInputHandler(element, mosaikRuntime)
                is StringTextField -> StringInputHandler(element)
                // TODO is ErgAmountInputField -> FiatOrErgTextInputHandler(element, mosaikRuntime)
                // TODO is DecimalInputField -> DecimalInputHandler(element, element.scale)
                // TODO is LongTextField -> IntegerInputHandler(element)
                // TODO is ErgoAddressChooseButton -> ErgoAddressChooserInputHandler(element, mosaikRuntime)
                // TODO is WalletChooseButton -> WalletChooserInputHandler(element, mosaikRuntime)
                // TODO is DropDownList -> DropDownListInputHandler(element, mosaikRuntime)
                is InputElement -> OtherInputHandler(element)
                else -> null
            }
    }
}

class StringInputHandler(private val element: StringTextField) :
    InputElementValueHandler<String>() {
    override fun isValueValid(value: Any?): Boolean {
        val length = (value as? String)?.length ?: 0
        return length >= element.minValue && length <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): String? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = when (element) {
            is PasswordInputField -> KeyboardType.Password
            else -> KeyboardType.Text
        }
}

open class OtherInputHandler(private val element: InputElement) :
    InputElementValueHandler<Any>() {
    override fun isValueValid(value: Any?): Boolean {
        return if (element is OptionalInputElement) !element.mandatory || value != null else true
    }

    override fun valueFromStringInput(value: String?): Any? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Text
}

enum class KeyboardType {
    Text,
    Number,
    NumberDecimal,
    Email,
    Password
}