import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.ergoplatform.toLongValueWithScale
import org.ergoplatform.toPlainStringFixed
import org.junit.Test
import kotlin.test.assertEquals

class BigNumberTest {
    @Test
    fun testBigNumbers() {
        val bigDecimal = "501.12345678".toBigDecimal()
        assertEquals(50112345678L, bigDecimal.toLongValueWithScale(8))
        assertEquals(5011234567800L, bigDecimal.toLongValueWithScale(10))
        assertEquals(501123456780L, bigDecimal.toLongValueWithScale(9))
        assertEquals(1123456783L, "1.1234567826".toBigDecimal().toLongValueWithScale(9)) // TODO should be an error
        assertEquals(0, "0".toBigDecimal().toLongValueWithScale(9))
    }

    @Test
    fun printNumberTest() {
        assertEquals("1.000000", 1000000.toBigDecimal().moveDecimalPoint(-6).scale(6).toPlainStringFixed(6))
    }
}