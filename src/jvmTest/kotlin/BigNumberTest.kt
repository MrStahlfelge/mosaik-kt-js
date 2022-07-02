import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import org.junit.Test
import kotlin.test.assertEquals

class BigNumberTest {
    @Test
    fun testBigNumbers() {
        val bigDecimal = "501.12345678".toBigDecimal(decimalMode = DecimalMode(10, RoundingMode.ROUND_HALF_CEILING, 10))
        val moveDecimalPoint8 = bigDecimal.moveDecimalPoint(8)
        val moveDecimalPoint9 = bigDecimal.moveDecimalPoint(9)
        val moveDecimalPoint10 = bigDecimal.moveDecimalPoint(10)
        assertEquals(50112345678L, moveDecimalPoint8.longValue())
        assertEquals(5011234567800L, moveDecimalPoint10.longValue())
        assertEquals(501123456780L, moveDecimalPoint9.longValue())
    }
}