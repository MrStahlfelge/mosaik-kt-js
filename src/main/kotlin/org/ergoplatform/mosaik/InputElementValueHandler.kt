package org.ergoplatform.mosaik

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.*
import org.ergoplatform.toLongValueWithScale

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
                is ErgAddressInputField -> ErgAddressTextInputHandler(element, mosaikRuntime)
                is StringTextField -> StringInputHandler(element)
                is ErgAmountInputField -> FiatOrErgTextInputHandler(element, mosaikRuntime)
                is DecimalInputField -> DecimalInputHandler(element, element.scale)
                is LongTextField -> IntegerInputHandler(element)
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

class ErgAddressTextInputHandler(
    private val element: ErgAddressInputField,
    private val runtime: MosaikRuntime
) : InputElementValueHandler<String>() {
    override fun isValueValid(value: Any?): Boolean {
        val addressString = (value as? String)

        return if (addressString == null) element.minValue <= 0
        else runtime.isErgoAddressValid(addressString)
    }

    override fun valueFromStringInput(value: String?): String? {
        return value?.let { if (runtime.isErgoAddressValid(value)) value else null }
    }

    override val keyboardType: KeyboardType get() = KeyboardType.Text
}

class IntegerInputHandler(private val element: LongTextField) : InputElementValueHandler<Long>() {
    override fun isValueValid(value: Any?): Boolean {
        return value is Long && value >= element.minValue && value <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): Long? {
        return value?.toLong()
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Number
}

open class DecimalInputHandler(private val element: LongTextField, private val scale: Int) :
    InputElementValueHandler<Long>() {

    override fun isValueValid(value: Any?): Boolean {
        return value is Long && value >= element.minValue && value <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): Long? {
        return if (value.isNullOrBlank()) null
        else {
            value.toBigDecimal().toLongValueWithScale(scale)
        }
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.NumberDecimal

    override fun getAsString(currentValue: Any?): String {
        return (currentValue as Long?)?.toBigDecimal()?.moveDecimalPoint(-scale)?.toPlainString() ?: ""
    }
}

class FiatOrErgTextInputHandler(
    private val element: LongTextField,
    private val mosaikRuntime: MosaikRuntime,
) : DecimalInputHandler(element, scaleErg) {

    var inputIsFiat = mosaikRuntime.preferFiatInput && canChangeInputMode()
        private set

    fun switchInputAmountMode() {
        inputIsFiat = if (canChangeInputMode()) {
            mosaikRuntime.preferFiatInput = !inputIsFiat
            !inputIsFiat
        } else
            false
    }

    fun canChangeInputMode() =
        element is FiatOrErgAmountInputField && mosaikRuntime.fiatRate != null

    override fun valueFromStringInput(value: String?): Long? {
        return if (inputIsFiat) {
            val fiatRate = mosaikRuntime.fiatRate
            if (value == null || fiatRate == null)
                null
            else
                try {
                    BigDecimal.parseString(value).divide(
                        fiatRate.toBigDecimal(),
                    ).toLongValueWithScale(scaleErg)
                } catch (t: Throwable) {
                    null
                }
        } else
            super.valueFromStringInput(value)
    }

    override fun getAsString(currentValue: Any?): String {
        return if (inputIsFiat) currentValue?.let {
            mosaikRuntime.convertErgToFiat(
                currentValue as Long,
                formatted = false
            )
        } ?: "" else super.getAsString(currentValue)
    }

    fun getSecondLabelString(nanoErg: Long): String? {
        return if (inputIsFiat)
            (nanoErg.toBigDecimal().moveDecimalPoint(-scaleErg)
                .scale(scaleformatShortErg.toLong()).toPlainString()) +
                    " $ergoCurrencyText"
        else
            mosaikRuntime.convertErgToFiat(nanoErg)
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