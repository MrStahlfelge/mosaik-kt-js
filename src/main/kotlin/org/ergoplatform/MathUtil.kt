package org.ergoplatform

import mikhaylutsyury.kigdecimal.BigDecimal

actual fun BigDecimal.toLongValueWithScale(scale: Int): Long {
    try {
        // we need to convert to string to get an actual long out of JS bigdecimal
        return this.movePointRight(scale).setScale(0).toPlainString().toLong()
    } catch (e: dynamic) {
        throw ArithmeticException(e.toString())
    }
}
