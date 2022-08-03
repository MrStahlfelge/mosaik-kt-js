package org.ergoplatform

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode


fun BigDecimal.toLongValueWithScale(scale: Int): Long {
    //val value = this.moveDecimalPoint(scale).longValue(true) <- gives wrong results, see BigNumberTest

    if (!moveDecimalPoint(scale).isWholeNumber()) {
        throw ArithmeticException()
    }

    val longScale = scale.toLong()
    val bigDecimalWhole = BigDecimal.fromBigDecimal(
        this,
        decimalMode = DecimalMode(longScale, RoundingMode.ROUND_HALF_CEILING, longScale)
    ).moveDecimalPoint(scale)

    return bigDecimalWhole.toBigInteger() // this one is needed because longValue(true) on BigDecimal takes ages on JS
        .longValue(true)

}

fun BigDecimal.toPlainStringFixed(scale: Int): String {
    // work around https://github.com/ergoplatform/ergo-wallet-app/issues/132
    var string = toPlainString()

    val numOfDecimals = string.substringAfter('.', "").length
    if (numOfDecimals < scale && scale > 0) {
        if (!string.contains('.')) {
            string += '.'
        }

        string += "0".repeat(scale - numOfDecimals)
    }

    return string
}