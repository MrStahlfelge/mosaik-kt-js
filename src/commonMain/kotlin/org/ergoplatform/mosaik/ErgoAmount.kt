package org.ergoplatform.mosaik

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.ergoplatform.toLongValueWithScale

const val nanoPowerOfTen = 9
const val ergoCurrencyText = "ERG"

class ErgoAmount(val nanoErgs: Long) {

    companion object {
        val ZERO = ErgoAmount(0)
    }

    constructor(bigDecimal: BigDecimal) :
            this(
                bigDecimal.toLongValueWithScale(nanoPowerOfTen)
            )

    constructor(ergString: String) : this(
        if (ergString.isBlank()) 0 else ergString.toBigDecimal()
            .toLongValueWithScale(nanoPowerOfTen)
    )

    override fun toString(): String {
        return toBigDecimal().toPlainString()
    }

    /**
     * converts the amount to a string rounded to given number of decimals places
     */
    fun toStringRoundToDecimals(numDecimals: Long, trimTrailingZeros: Boolean): String {
        val numAsString = toBigDecimal()
            .scale(numDecimals)
            .toPlainString()

        return if (trimTrailingZeros)
            numAsString.trimEnd('0').trimEnd('.')
        else numAsString
    }

    fun toBigDecimal() = nanoErgs.toBigDecimal().moveDecimalPoint(-nanoPowerOfTen)

    /**
     * @return double amount, only for representation purposes because double has floating point issues
     */
    fun toDouble(): Double {
        val microErgs = nanoErgs / (1000L * 100L)
        val ergs = microErgs.toDouble() / 10000.0
        return ergs
    }
}

fun String.toErgoAmount(): ErgoAmount? {
    try {
        return ErgoAmount(this)
    } catch (t: Throwable) {
        return null
    }
}