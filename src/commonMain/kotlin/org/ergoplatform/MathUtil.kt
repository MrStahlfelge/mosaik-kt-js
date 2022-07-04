package org.ergoplatform

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode


fun BigDecimal.toLongValueWithScale(scale: Int): Long {
    val longScale = scale.toLong()
    //val value = this.moveDecimalPoint(scale).longValue(true) <- gives wrong results, see BigNumberTest
    val bigDecimalWhole = BigDecimal.fromBigDecimal(
        this,
        decimalMode = DecimalMode(longScale, RoundingMode.ROUND_HALF_CEILING, longScale)
    ).moveDecimalPoint(scale)
//
//    if (!bigDecimalWhole.isWholeNumber()) { // FIXME does not work https://github.com/ionspin/kotlin-multiplatform-bignum/issues/238
//        throw ArithmeticException()
//    }

    return bigDecimalWhole.toBigInteger() // this one is needed because longValue(true) on BigDecimal takes ages on JS
        .longValue(true)

}