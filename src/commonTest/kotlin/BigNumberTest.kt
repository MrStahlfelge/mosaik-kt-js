import mikhaylutsyury.kigdecimal.toBigDecimal
import org.ergoplatform.toLongValueWithScale
import kotlin.test.Test
import kotlin.test.assertEquals

class BigNumberTest {
    @Test
    fun testBigNumbers() {
        val bigDecimal = "501.12345678".toBigDecimal()
        assertEquals(50112345678L, bigDecimal.toLongValueWithScale(8))
        assertEquals(5011234567800L, bigDecimal.toLongValueWithScale(10))
        assertEquals(501123456780L, bigDecimal.toLongValueWithScale(9))
        assertEquals(
            true,
            try {
                "1.1234567826".toBigDecimal().toLongValueWithScale(9)
                false
            } catch (t: ArithmeticException) {
                true
            }
        )
        assertEquals(0, "0".toBigDecimal().toLongValueWithScale(9))
        assertEquals(500230, "5002.3".toBigDecimal().toLongValueWithScale(2))
        assertEquals(500231, "5002.31".toBigDecimal().toLongValueWithScale(2))
        assertEquals(100000, "100000".toBigDecimal().toLongValueWithScale(0))
        assertEquals(Int.MAX_VALUE * 10L, Int.MAX_VALUE.toString().toBigDecimal().toLongValueWithScale(1))
    }

    @Test
    fun printNumberTest() {
        assertEquals("1.000000", 1000000.toBigDecimal().movePointLeft(6).setScale(6).toPlainString())
    }


    @Test
    fun testIsWhole() {
        val bigDecimalWhole = "1.1234567826".toBigDecimal().movePointRight(9).setScale(18)

        assertEquals("1123456782.600000000000000000", bigDecimalWhole.toPlainString())
    }
}