package org.ergoplatform.mosaik

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

const val nanoPowerOfTen = 9L
const val ergoCurrencyText = "ERG"

// FIXME see BigNumberTest - some numbers fail without DecimalMode set
// but with DecimalMode set, too many decimals are not detected any more
// https://github.com/ionspin/kotlin-multiplatform-bignum/issues/237

class ErgoAmount(val nanoErgs: Long) {

    companion object {
        val ZERO = ErgoAmount(0)
    }

    constructor(bigDecimal: BigDecimal) :
            this(
                bigDecimal.scale(nanoPowerOfTen)
                    .moveDecimalPoint(nanoPowerOfTen).longValue(true)
            )

    constructor(ergString: String) : this(
        if (ergString.isBlank()) 0 else ergString.toBigDecimal()
            .moveDecimalPoint(nanoPowerOfTen).longValue(true)
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