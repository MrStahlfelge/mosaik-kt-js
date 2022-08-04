package org.ergoplatform

import mikhaylutsyury.kigdecimal.BigDecimal

actual fun BigDecimal.toLongValueWithScale(scale: Int): Long {
    return this.movePointRight(scale).setScale(0).longValueExact()
}
